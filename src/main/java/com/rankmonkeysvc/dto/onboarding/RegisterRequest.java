package com.rankmonkeysvc.dto.onboarding;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterRequest {
    private String email;
    private String authCode;
    private String password;
}