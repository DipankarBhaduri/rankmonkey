package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.auth.impl.JwtSvcImpl;
import com.rankmonkeysvc.constants.EventType;
import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.onboarding.RegisterRequest;
import com.rankmonkeysvc.exceptions.DuplicateEmailException;
import com.rankmonkeysvc.exceptions.IncorrectCredentialsException;
import com.rankmonkeysvc.exceptions.UserNotFoundException;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.AuthSvc;
import com.rankmonkeysvc.utils.EventLogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.UUID;
import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorMessages.*;


@Service
@Slf4j
public class AuthSvcImpl implements AuthSvc {
    private final UserInfoRepository userInfoRepository;
    private final JwtSvcImpl jwtSvc;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EventLogHelper eventLogHelper;

    @Autowired
    public AuthSvcImpl(
            UserInfoRepository userInfoRepository,
            JwtSvcImpl jwtSvc,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EventLogHelper eventLogHelper
    ) {
        this.userInfoRepository = userInfoRepository;
        this.jwtSvc = jwtSvc;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.eventLogHelper = eventLogHelper;
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
                .setRole(Role.USER)
                .setStatus(UserStatus.NEW)
                .setFirstName(registerRequest.getFirstName())
                .setLastName(registerRequest.getLastName());

        eventLogHelper.createEventLog(
                EventType.USER_CREATION, userObject.getId().toString(), new HashMap<>(), null);

        userInfoRepository.save(userObject);
        String jwtToken = jwtSvc.generateToken(userObject);
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

        String jwtToken = jwtSvc.generateToken(user);
        eventLogHelper.createEventLog(
                EventType.USER_LOGIN, user.getId().toString(), new HashMap<>(), null);

        return new AuthenticationResponse().setAccessToken(jwtToken);
    }
}