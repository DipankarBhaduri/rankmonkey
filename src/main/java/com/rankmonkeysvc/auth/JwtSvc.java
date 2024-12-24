package com.rankmonkeysvc.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtSvc {
    String generateToken(UserDetails user, Long tokenExpiration);
	String getUser(HttpServletRequest request);
    String extractUsername(String token);
}