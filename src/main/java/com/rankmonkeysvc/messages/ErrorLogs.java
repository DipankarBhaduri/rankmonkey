package com.rankmonkeysvc.messages;

public class ErrorLogs {
    public static final String ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: Error during postForObject API call.";
    public static final String PRINT_LOG_WITH_ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: error during postForObject :: {}.";
    public static final String LOG_WITH_PAYLOAD = "ERROR :: user :: {} :: record :: {}.";
    public static final String USER_NOT_FOUND_WITH_EMAIL_ID = "ERROR :: User not found with email: {}.";
    public static final String BAD_CREDENTIALS_ERROR_LOG = "ERROR :: either username :: %s or password %s is incorrect.";
    public static final String INCORRECT_OLD_PASSWORD_LOG = "ERROR :: Incorrect old password for user - {}.";
    public static final String EMAIL_CANNOT_BE_EMPTY = "ERROR :: email cannot be empty.";
    public static final String INVALID_EMAIL_FORMAT = "ERROR :: {} its a invalid email format.";
    public static final String DATABASE_OPERATION_FAILED = "ERROR :: {} - Database connection failed.";
    public static final String INCORRECT_RESULT_SIZE_DATA_ACCESS_EXCEPTION = "ERROR :: Expected single result but got multiple - {}";
    public static final String MAXIMUM_RETRY_COUNT_EXCEEDED = "ERROR :: with in 24 hrs user trying to trying more then 3 times, user - {}";
    public static final String AUTH_TOKEN_IS_INVALID = "ERROR :: auth-token is invalid for user - {}";
    public static final String LINK_HAS_EXPIRED = "ERROR :: link has expired for user - {}";
    public static final String NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME_LOG = "ERROR :: new password and old password cannot be same for user - {}";
}