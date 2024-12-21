package com.rankmonkeysvc.messages;

public class ErrorLogs {
    public static final String ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: Error during postForObject API call.";
    public static final String PRINT_LOG_WITH_ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: error during postForObject :: {}.";
    public static final String LOG_WITH_PAYLOAD = "ERROR :: user :: {} :: record :: {}.";
    public static final String USER_NOT_FOUND_WITH_EMAIL_ID = "ERROR :: User not found with email: {}.";
    public static final String BAD_CREDENTIALS_ERROR_LOG = "ERROR :: either username :: %s or password %s is incorrect.";
    public static final String EMAIL_SYNCED_WITH_ANOTHER_ACCOUNT = "ERROR :: {} is already synced with another account.";
}