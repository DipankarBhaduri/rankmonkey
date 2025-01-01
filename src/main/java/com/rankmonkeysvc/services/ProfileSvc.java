package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.auth.PasswordChangeRequest;
import com.rankmonkeysvc.dto.common.MessageResponse;

public interface ProfileSvc {
    MessageResponse changePassword(
            String userId,
            PasswordChangeRequest passwordChangeRequest
    );
}