package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.auth.impl.JwtSvcImpl;
import com.rankmonkeysvc.constants.EventType;
import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.EmailExistsResponse;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.exceptions.*;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.AuthSvc;
import com.rankmonkeysvc.services.EmailSvc;
import com.rankmonkeysvc.utils.EventLogHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorLogs.DATABASE_OPERATION_FAILED;
import static com.rankmonkeysvc.messages.ErrorMessages.*;
import static com.rankmonkeysvc.messages.StaticMessages.*;
import static com.rankmonkeysvc.messages.SuccessMessages.*;

@Service
@Slf4j
public class AuthSvcImpl implements AuthSvc {
    private final UserInfoRepository userInfoRepository;
    private final JwtSvcImpl jwtSvc;
    private final EmailSvc emailSvc;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EventLogHelper eventLogHelper;

    @Autowired
    public AuthSvcImpl(
            UserInfoRepository userInfoRepository,
            JwtSvcImpl jwtSvc,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EventLogHelper eventLogHelper,
            EmailSvc emailSvc
    ) {
        this.userInfoRepository = userInfoRepository;
        this.jwtSvc = jwtSvc;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.eventLogHelper = eventLogHelper;
        this.emailSvc = emailSvc;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			log.error(String.format(
                    BAD_CREDENTIALS_ERROR_LOG, request.getEmail(), request.getPassword())
			);
			throw new IncorrectCredentialsException(INCORRECT_CREDENTIALS);
		}

        var user = userInfoRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error(
                            String.format(USER_NOT_FOUND_WITH_EMAIL_ID, request.getEmail())
                    );
                    return new UserNotFoundException(USER_NOT_FOUND);
                });

        eventLogHelper.createEventLog(
                EventType.USER_LOGIN, user.getId().toString(), new HashMap<>(), null);

        String jwtAccessToken = jwtSvc.generateToken(user, ACCESS_TOKEN_EXPIRY);
        String jwtRequestToken = jwtSvc.generateToken(user, REFRESH_TOKEN_EXPIRY);

        return new AuthenticationResponse()
                .setAccessToken(jwtAccessToken)
                .setRefreshToken(jwtRequestToken)
                .setBaseUrl(ADMIN_BASE_URL)
                .setMessage(LOGIN_SUCCESSFULLY);
    }

    @Override
    public EmailExistsResponse checkEmail(String email) {
        validateEmail(email);
        EmailExistsResponse response = new EmailExistsResponse();

        try {
            userInfoRepository.findByEmailAndEmailVerifiedTrue(email).ifPresentOrElse(
                    userInfo -> {
                        response.setEmailExists(true)
                                .setRole(userInfo.getRole())
                                .setMessage(ENTER_YOUR_PASSWORD)
                                .setStatus(userInfo.getStatus().name());
                    },
                    () -> {
                        userInfoRepository.findByEmailAndPasswordIsNull(email).ifPresentOrElse(
                                user -> {
                                    validateRetryCountAndUpdate(user, response);
                                    sendEmailAsync(email, user);
                                },
                                () -> {
                                    response.setEmailExists(false)
                                            .setMessage(EMAIL_SENT_TO_YOUR_INBOX);

                                    User userObject = new User()
                                            .setId(UUID.randomUUID())
                                            .setEmail(email)
                                            .setRole(Role.ADMIN)
                                            .setRetryCount(1)
                                            .setPasswordResetLinkGeneratedAt(LocalDateTime.now())
                                            .setStatus(UserStatus.NEW);

                                    eventLogHelper.createEventLog(
                                            EventType.USER_CREATION,
                                            userObject.getId().toString(), new HashMap<>(), null);

                                    User user = userInfoRepository.save(userObject);
                                    sendEmailAsync(email, user);
                                }
                        );
                    }
            );
        } catch (MaximumRetryCountExceededException e) {
            throw new MaximumRetryCountExceededException(e.getMessage());

        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(
                    String.format(INCORRECT_RESULT_SIZE_DATA_ACCESS_EXCEPTION, e.getMessage())
            );
            throw new IncorrectResultSizeException(INCORRECT_RESULT_SIZE_EXCEPTION);
        } catch (Exception e) {
            log.error(
                    String.format(DATABASE_OPERATION_FAILED, e.getMessage())
            );
            throw new DatabaseOperationException(DATABASE_OPERATION_FAIL);
        }
        return response;
    }

    private void validateRetryCountAndUpdate(
            User user, EmailExistsResponse response
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime resetLinkTimeLimit = now.minusDays(1);

        if (user.getRetryCount() >= MAXIMUM_RETRY_COUNT_FOR_RESET_OR_FORGET_PASSWORD &&
                user.getPasswordResetLinkGeneratedAt().isAfter(resetLinkTimeLimit)) {
            log.error(
                    String.format(MAXIMUM_RETRY_COUNT_EXCEEDED, user.getEmail())
            );
            throw new MaximumRetryCountExceededException(MAXIMUM_RETRY_COUNT_EXCEEDED_MESSAGE);
        }

        user.setRetryCount(user.getRetryCount() == MAXIMUM_RETRY_COUNT_FOR_RESET_OR_FORGET_PASSWORD ? 1 : user.getRetryCount() + 1)
                .setPasswordResetLinkGeneratedAt(now);

        response.setEmailExists(false)
                .setMessage(EMAIL_SENT_TO_YOUR_INBOX);

        userInfoRepository.save(user);
    }

    private void sendEmailAsync(String email, User user) {
        eventLogHelper.createEventLog(
                EventType.SET_PASSWORD_REQUEST_VIA_EMAIL,
                user.getId().toString(), new HashMap<>(), null);

        CompletableFuture.runAsync(
                () -> {
                    emailSvc.sendEmail(email, user);
        });
    }

    private static void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            log.error(
                    EMAIL_CANNOT_BE_EMPTY
            );
            throw new InvalidEmailException(EMPTY_EMAIL);
        }

        if (!email.matches(EMAIL_REGEX)) {
            log.error(
                    String.format(INVALID_EMAIL_FORMAT, email)
            );
            throw new InvalidEmailFormatException(WRONG_EMAIL_FORMAT);
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

        if (!PUBLIC_EMAIL_DOMAINS.contains(domain)) {
            log.error(
                    String.format(INVALID_EMAIL_FORMAT, email)
            );
            throw new InvalidEmailFormatException(EMAIL_FORMAT_NOT_ALLOWED);
        }
    }

    @Override
    public AuthenticationResponse setPassword(String password, String authToken) {
        boolean isValid = validatePassword(password);
        if (!isValid) {
            log.error(
                    String.format(PASSWORD_IS_INVALID, authToken)
            );
            throw new InvalidPasswordException(INVALID_PASSWORD);
        }

        try {
            Optional<User> userOptional = userInfoRepository.findById(UUID.fromString(authToken));
            if (userOptional.isPresent()) {
                return setPassword(password, userOptional.get());

            } else {
                log.error(AUTH_TOKEN_IS_INVALID, authToken);
                throw new InvalidAuthTokenException(TOKEN_IS_INVALID_MESSAGE);

            }
        } catch (LinkExpiredException e) {
            throw new LinkExpiredException(e.getMessage());

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof InvalidAuthTokenException) {
                log.error(
                        AUTH_TOKEN_IS_INVALID, authToken
                );
                throw new InvalidAuthTokenException(TOKEN_IS_INVALID_MESSAGE);
            }
            log.error(
                    String.format(DATABASE_OPERATION_FAILED, e.getMessage())
            );
            throw new DatabaseOperationException(DATABASE_OPERATION_FAIL);
        }
    }

    private boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }

        return password.matches(PASSWORD_REGEX);
    }

    private AuthenticationResponse setPassword(String password, User user) {
        LocalDateTime now = LocalDateTime.now();
        if (user.getPasswordResetLinkGeneratedAt().isBefore(now.minusMinutes(SET_PASSWORD_EMAIL_EXPIRY)) ||
                StringUtils.isNotEmpty(user.getPassword())) {
            log.error(
                    String.format(LINK_HAS_EXPIRED, user.getEmail())
            );
            throw new LinkExpiredException(LINK_HAS_EXPIRED_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(password))
                .setEmailVerified(true);

        userInfoRepository.save(user);
        eventLogHelper.createEventLog(
                EventType.USER_LOGIN, user.getId().toString(), new HashMap<>(), null);

        String jwtAccessToken = jwtSvc.generateToken(user, ACCESS_TOKEN_EXPIRY);
        String jwtRequestToken = jwtSvc.generateToken(user, REFRESH_TOKEN_EXPIRY);

        return new AuthenticationResponse()
                .setAccessToken(jwtAccessToken)
                .setRefreshToken(jwtRequestToken)
                .setBaseUrl(ADMIN_BASE_URL)
                .setMessage(PASSWORD_SET_SUCCESSFULLY);
    }

    @Override
    public MessageResponse forgetPassword(String email) {
        try {
            userInfoRepository.findByEmailAndEmailVerifiedTrue(email).ifPresentOrElse(
                    user -> {
                        validateRetryCountAndUpdate(user, new EmailExistsResponse());
                        sendEmailAsync(email, user);
                    },
                    () -> {
                        log.error(
                                String.format(USER_NOT_FOUND_WITH_EMAIL_ID, email)
                        );
                        throw new UserNotFoundException(USER_NOT_FOUND);
                    }
            );
        } catch (MaximumRetryCountExceededException e) {
                throw new MaximumRetryCountExceededException(e.getMessage());

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());

        } catch (Exception e) {
            log.error(
                    String.format(DATABASE_OPERATION_FAILED, e.getMessage())
            );
            throw new DatabaseOperationException(DATABASE_OPERATION_FAIL);
        }
        return new MessageResponse().setMessage(PASSWORD_RESET_LINK_SENT);
    }

    @Override
    public AuthenticationResponse tokenExchange(String refreshToken) {
        String email = null;
        try {
            email = jwtSvc.extractUsername(refreshToken);

        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpired(REFRESH_TOKEN_EXPIRED);

        } catch (SignatureException e) {
            log.error(
                    TOKEN_IS_INVALID, email
            );
            throw new InvalidTokenException(TOKEN_IS_INVALID_MESSAGE);
        }

        String finalEmail = email;
        User user = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(String.format(
                            USER_NOT_FOUND_WITH_EMAIL_ID, finalEmail)
                    );
                    return new UserNotFoundException(USER_NOT_FOUND);
                });

        String jwtAccessToken = jwtSvc.generateToken(user, ACCESS_TOKEN_EXPIRY);
        String jwtRefreshToken = jwtSvc.generateToken(user, REFRESH_TOKEN_EXPIRY);

        return new AuthenticationResponse()
                .setAccessToken(jwtAccessToken)
                .setRefreshToken(jwtRefreshToken)
                .setBaseUrl(ADMIN_BASE_URL)
                .setMessage(LOGIN_SUCCESSFULLY);
    }
}