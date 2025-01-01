package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.auth.JwtSvc;
import com.rankmonkeysvc.dto.auth.PasswordChangeRequest;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.services.ProfileSvc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.rankmonkeysvc.messages.InfoLogs.CHANGE_PASSWORD;

@Slf4j
@RestController
@RequestMapping("/v1/profile")
public class ProfileController {

    private final ProfileSvc profileSvc;
    private final JwtSvc jwtSvc;

    @Autowired
    public ProfileController(
            ProfileSvc profileSvc,
            JwtSvc jwtSvc
    ) {
        this.profileSvc = profileSvc;
        this.jwtSvc = jwtSvc;
    }

    @PutMapping("/change-password")
    public MessageResponse changePassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            HttpServletRequest request
    ) {
        String userId = jwtSvc.getUser(request);
        log.info(CHANGE_PASSWORD, userId);
        return profileSvc.changePassword(userId, passwordChangeRequest);
    }
}