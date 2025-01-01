package com.rankmonkeysvc.auth;

import com.rankmonkeysvc.dao.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtSvc {
    String generateToken(UserDetails user, Long tokenExpiration);
	String getUser(HttpServletRequest request);
    String extractUsername(String token);
    String generateDummyTokenWithHS256(User user, boolean admin);
}