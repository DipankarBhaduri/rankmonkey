package com.rankmonkeysvc.dto.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}