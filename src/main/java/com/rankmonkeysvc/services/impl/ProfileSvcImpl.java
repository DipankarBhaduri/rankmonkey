package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.auth.PasswordChangeRequest;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.exceptions.DatabaseOperationException;
import com.rankmonkeysvc.exceptions.IncorrectCredentialsException;
import com.rankmonkeysvc.exceptions.OldAndNewSamePasswordException;
import com.rankmonkeysvc.exceptions.UserNotFoundException;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.ProfileSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;
import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorMessages.*;
import static com.rankmonkeysvc.messages.SuccessMessages.PASSWORD_CHANGE_SUCCESSFULLY;

@Slf4j
@Component
public class ProfileSvcImpl implements ProfileSvc {

    private final UserInfoRepository userInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileSvcImpl(
            UserInfoRepository userInfoRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userInfoRepository = userInfoRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MessageResponse changePassword(String userId, PasswordChangeRequest passwordChangeRequest) {
        UUID uuid = UUID.fromString(userId);
        User user = userInfoRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), passwordChangeRequest.getOldPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error(String.format(INCORRECT_OLD_PASSWORD_LOG, user.getEmail()));
            throw new IncorrectCredentialsException(INCORRECT_OLD_PASSWORD);
        }

        if (passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getOldPassword())) {
            log.error(NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME_LOG, user.getEmail());
            throw new OldAndNewSamePasswordException(NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userInfoRepository.save(user);

        return new MessageResponse().setMessage(PASSWORD_CHANGE_SUCCESSFULLY);
    }
}