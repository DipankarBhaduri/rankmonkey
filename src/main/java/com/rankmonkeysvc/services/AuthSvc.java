package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.EmailExistsResponse;
import com.rankmonkeysvc.dto.onboarding.RegisterRequest;

public interface AuthSvc {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    EmailExistsResponse checkEmail(String email);

    AuthenticationResponse setPassword(String password, String authToken);
}