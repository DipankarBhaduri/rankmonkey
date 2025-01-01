package com.rankmonkeysvc.dto.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
    private String baseUrl;
}