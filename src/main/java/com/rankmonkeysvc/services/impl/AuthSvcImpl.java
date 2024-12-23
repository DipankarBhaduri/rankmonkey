package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.auth.impl.JwtSvcImpl;
import com.rankmonkeysvc.constants.EventType;
import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.EmailExistsResponse;
import com.rankmonkeysvc.dto.onboarding.RegisterRequest;
import com.rankmonkeysvc.exceptions.*;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.AuthSvc;
import com.rankmonkeysvc.services.EmailSvc;
import com.rankmonkeysvc.utils.EventLogHelper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorLogs.DATABASE_OPERATION_FAILED;
import static com.rankmonkeysvc.messages.ErrorMessages.*;
import static com.rankmonkeysvc.messages.StaticMessages.EMAIL_REGEX;

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
    public AuthenticationResponse register(RegisterRequest registerRequest){
		userInfoRepository.findByEmail(registerRequest.getEmail())
				.ifPresent(user -> {
					log.error(EMAIL_SYNCED_WITH_ANOTHER_ACCOUNT, registerRequest.getEmail());
                    throw new DuplicateEmailException(DUPLICATE_EMAIL);
				});
		
		User userObject = new User().setId(UUID.randomUUID())
                .setEmail(registerRequest.getEmail())
                .setPassword(passwordEncoder.encode(registerRequest.getPassword()))
                .setRole(Role.ADMIN)
                .setStatus(UserStatus.NEW);

        eventLogHelper.createEventLog(
                EventType.USER_CREATION, userObject.getId().toString(), new HashMap<>(), null);

        userInfoRepository.save(userObject);
        String jwtToken = jwtSvc.generateToken(userObject, 123L);
        return new AuthenticationResponse().setAccessToken(jwtToken);
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
                               log.error(USER_NOT_FOUND_WITH_EMAIL_ID, request.getEmail());
                               return new UserNotFoundException(USER_NOT_FOUND);
                           });

        String jwtToken = jwtSvc.generateToken(user, 123L);
        eventLogHelper.createEventLog(
                EventType.USER_LOGIN, user.getId().toString(), new HashMap<>(), null);

        return new AuthenticationResponse().setAccessToken(jwtToken);
    }

    @Override
    public EmailExistsResponse checkEmail(String email) {
        validateEmail(email);
        EmailExistsResponse response = new EmailExistsResponse();

        try {
            userInfoRepository.findByEmailAndEmailVerifiedTrue(email).ifPresentOrElse(
                    userInfo -> {
                        response.setEmailExists(true);
                        response.setRole(userInfo.getRole());
                        response.setStatus(userInfo.getStatus().name());
                    },
                    () -> {
                        userInfoRepository.findByEmailAndPasswordIsNull(email).ifPresentOrElse(
                                user -> {
                                    response.setEmailExists(false);
                                    userInfoRepository.save(user.setUpdatedAt(LocalDateTime.now()));
                                    CompletableFuture.runAsync(() -> {
                                        emailSvc.sendEmail(email, user.getId().toString());
                                    });
                                },
                                () -> {
                                    response.setEmailExists(false);
                                    User userObject = new User()
                                            .setId(UUID.randomUUID()).setEmail(email).setStatus(UserStatus.NEW);

                                    User user = userInfoRepository.save(userObject);
                                    CompletableFuture.runAsync(() -> {
                                        emailSvc.sendEmail(email, user.getId().toString());
                                    });
                                }
                        );
                    }
            );
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

    private static void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            log.error(EMAIL_CANNOT_BE_EMPTY);
            throw new InvalidEmailException(EMPTY_EMAIL);
        }

        if (!email.matches(EMAIL_REGEX)) {
            log.error(String.format(INVALID_EMAIL_FORMAT, email));
            throw new InvalidEmailFormatException(WRONG_EMAIL_FORMAT);
        }
    }

    @Override
    public AuthenticationResponse setPassword(String password, String authToken) {
        return null;
    }
}