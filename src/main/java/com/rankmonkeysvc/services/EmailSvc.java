package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.common.MessageResponse;

public interface EmailSvc {
    MessageResponse sendEmail(
            String email,
            User user
    );
}
