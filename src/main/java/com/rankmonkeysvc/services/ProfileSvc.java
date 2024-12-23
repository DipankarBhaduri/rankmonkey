package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.auth.AuthenticationResponse;
import com.rankmonkeysvc.dto.auth.PasswordChangeRequest;

public interface ProfileSvc {
    AuthenticationResponse changePassword(String userId, PasswordChangeRequest passwordChangeRequest);
}