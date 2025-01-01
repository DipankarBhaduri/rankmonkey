package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.EmailExistsResponse;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.services.AuthSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.rankmonkeysvc.messages.InfoLogs.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthSvc authSvc;

    @Autowired
    public AuthController(
            AuthSvc authSvc
    ) {
        this.authSvc = authSvc;
    }

    @GetMapping("/check-email")
    public EmailExistsResponse checkEmail(
            @RequestParam String email
    ) {
        log.info(CHECK_EMAIL, email);
        return authSvc.checkEmail(email);
    }

    @PutMapping("/set-password")
    public AuthenticationResponse setPassword(
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String authToken
    ) {
        log.info(SET_PASSWORD, authToken);
        return authSvc.setPassword(password, authToken);
    }

    @PostMapping("/forget-password")
    public MessageResponse forgetPassword(
            @RequestParam String email
    ) {
        log.info(REGISTER_FOR_USER, email);
        return authSvc.forgetPassword(email);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.info(AUTHENTICATE_FOR_USER, request.getEmail());
        return authSvc.authenticate(request);
    }

    @PostMapping("/token-exchange")
    public AuthenticationResponse tokenExchange(
            @RequestParam (required = true) String refreshToken
    ) {
        log.info(REFRESH_TOKEN, refreshToken);
        return authSvc.tokenExchange(refreshToken);
    }
}