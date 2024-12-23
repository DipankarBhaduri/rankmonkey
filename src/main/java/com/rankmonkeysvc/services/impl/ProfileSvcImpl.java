package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.PasswordChangeRequest;
import com.rankmonkeysvc.exceptions.IncorrectCredentialsException;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.ProfileSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.UUID;
import static com.rankmonkeysvc.messages.ErrorLogs.BAD_CREDENTIALS_ERROR_LOG;
import static com.rankmonkeysvc.messages.ErrorMessages.INCORRECT_CREDENTIALS;
import static com.rankmonkeysvc.messages.ErrorMessages.INCORRECT_OLD_PASSWORD;

@Slf4j
@Component
public class ProfileSvcImpl implements ProfileSvc {

    private final UserInfoRepository userInfoRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public ProfileSvcImpl (
        UserInfoRepository userInfoRepository,
        AuthenticationManager authenticationManager
    ) {
        this.userInfoRepository = userInfoRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse changePassword(String userId, PasswordChangeRequest passwordChangeRequest) {
        User user = userInfoRepository.findById(UUID.fromString(userId)).orElse(new User());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            passwordChangeRequest.getOldPassword()
                    )
            );

            if (passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getOldPassword())) {
                log.error(INCORRECT_OLD_PASSWORD);
                throw new IncorrectCredentialsException(INCORRECT_OLD_PASSWORD);
            }
        } catch (BadCredentialsException e) {
            log.error(String.format(
                    BAD_CREDENTIALS_ERROR_LOG, user.getEmail(), passwordChangeRequest.getOldPassword())
            );
            throw new IncorrectCredentialsException(INCORRECT_OLD_PASSWORD);
        }

        return null;
    }
}