package com.rankmonkeysvc.messages;

import java.util.Set;

public class StaticMessages {
    public static final String ADMIN_BASE_URL = "https://92d4-103-125-161-74.ngrok-free.app";
    public static final String ASSESSMENT_PLATFORM_BASE_URL = "http://localhost:3000";

    public static final String AUTHORIZATION = "Authorization";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";
    public static final String AUTH_TOKEN = "authToken";
    public static final String BEARER = "Bearer ";
    public static final int BEARER_INDEX = 7;
    public static final Long ACCESS_TOKEN_EXPIRY = 3600000L;
    public static final Long REFRESH_TOKEN_EXPIRY = 604800000L;
    public static final String COMA_SEPARATED = ",";
    public static final String SECRET_KEY = "9a2f8c4e6b0d71f3e8b925a45747f894a3d6bc70fa8d5e21a15a6d8c3b9a0e7c";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    public static final String APP_PASSWORD = "ddea rwud zgvi zqre";
    public static final String SENDER_EMAIL = "dipankarbhaduri8@gmail.com";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String TRUE = "TRUE";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_GMAIL_DOT_COM = "smtp.gmail.com";
    public static final String MAIL_GMAIL_PORT = "mail.smtp.port";
    public static final String MAIL_GMAIL_PORT_NUMBER = "587";
    public static final String MAIL_CONTENT_TYPE = "text/html";
    public static final String VERIFY_EMAIL_USING_EMAIL = "Welcome to RankMonkey! Verify Your Email to Get Started";
    public static final String CORS_MAPPING = "/**";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, PATCH";
    public static final String ALLOWED_HEADERS = "*";
    public static final boolean ALLOW_CREDENTIALS = false;
    public static final int MAX_AGE = 3600;
    public static final int SET_PASSWORD_EMAIL_EXPIRY = 5;
    public static final int MAXIMUM_RETRY_COUNT_FOR_RESET_OR_FORGET_PASSWORD = 3;
    public static final Set<String> PUBLIC_EMAIL_DOMAINS = Set.of(
            "gmail.com",
            "yahoo.com",
            "outlook.com",
            "hotmail.com",
            "aol.com"
    );
}