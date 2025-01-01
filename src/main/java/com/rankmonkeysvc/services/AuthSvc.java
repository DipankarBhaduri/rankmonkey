package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.auth.AuthenticationRequest;
import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.EmailExistsResponse;
import com.rankmonkeysvc.dto.common.MessageResponse;

public interface AuthSvc {
    AuthenticationResponse authenticate(
            AuthenticationRequest request
    );

    EmailExistsResponse checkEmail(
            String email
    );

    AuthenticationResponse setPassword(
            String password,
            String authToken
    );

    MessageResponse forgetPassword(
            String email
    );

    AuthenticationResponse tokenExchange(
            String refreshToken
    );
}