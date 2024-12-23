package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.common.MessageResponse;

public interface EmailSvc {
    MessageResponse sendEmail(String email, String authToken);
}
