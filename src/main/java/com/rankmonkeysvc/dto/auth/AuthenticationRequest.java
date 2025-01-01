package com.rankmonkeysvc.dto.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationRequest {
    private String email;
    private String password;
}
