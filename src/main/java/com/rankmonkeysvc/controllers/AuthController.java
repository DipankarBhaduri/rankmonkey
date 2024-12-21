package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.onboarding.RegisterRequest;
import com.rankmonkeysvc.services.AuthSvc;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.rankmonkeysvc.messages.InfoLogs.AUTHENTICATE_FOR_USER;
import static com.rankmonkeysvc.messages.InfoLogs.REGISTER_FOR_USER;

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

    @PostMapping("/register")
    public AuthenticationResponse register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        log.info(REGISTER_FOR_USER, registerRequest.getEmail());
        return authSvc.register(registerRequest);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.info(AUTHENTICATE_FOR_USER, request.getEmail());
        return authSvc.authenticate(request);
    }
}