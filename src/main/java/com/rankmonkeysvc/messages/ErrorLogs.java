package com.rankmonkeysvc.messages;

public class ErrorLogs {
    public static final String ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: Error during postForObject API call.";
    public static final String PRINT_LOG_WITH_ERROR_MESSAGE_FOR_POST_REQUEST = "ERROR :: error during postForObject :: {}.";
    public static final String LOG_WITH_PAYLOAD = "ERROR :: user :: {} :: record :: {}.";
    public static final String USER_NOT_FOUND_WITH_EMAIL_ID = "ERROR :: User not found with email: %s.";
    public static final String USER_DOES_NOT_HAVE_CREATION_ACCESS = "ERROR :: User does not have creation access for job-role: %s.";
    public static final String BAD_CREDENTIALS_ERROR_LOG = "ERROR :: either username :: %s or password %s is incorrect.";
    public static final String INCORRECT_OLD_PASSWORD_LOG = "ERROR :: Incorrect old password for user - %s.";
    public static final String EMAIL_CANNOT_BE_EMPTY = "ERROR :: email cannot be empty.";
    public static final String INVALID_EMAIL_FORMAT = "ERROR :: %s its a invalid email format.";
    public static final String DATABASE_OPERATION_FAILED = "ERROR :: %s - Database connection failed.";
    public static final String INCORRECT_RESULT_SIZE_DATA_ACCESS_EXCEPTION = "ERROR :: Expected single result but got multiple - %s";
    public static final String MAXIMUM_RETRY_COUNT_EXCEEDED = "ERROR :: with in 24 hrs user trying to generate set password more then 3 times, user - %s";
    public static final String PASSWORD_IS_INVALID = "ERROR :: password is invalid for user - %s";
    public static final String TOKEN_IS_INVALID = "ERROR :: token is invalid for user - {}";
    public static final String AUTH_TOKEN_IS_INVALID = "ERROR :: auth-token is invalid for user - {}";
    public static final String LINK_HAS_EXPIRED = "ERROR :: link has expired for user - %s";
    public static final String NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME_LOG = "ERROR :: new password and old password cannot be same for user - {}";
    public static final String JOB_ROLE_ALREADY_EXISTS = "ERROR :: Job role already exists - {}";
    public static final String SKILL_ALREADY_EXISTS = "ERROR :: Skill already exists - {}";
    public static final String JOB_ROLE_ID_IS_INVALID = "ERROR :: Job role id is invalid - {}";
    public static final String PREVIOUS_STATUS_AND_CURRENT_STATUS_ARE_SAME = "ERROR :: Previous status and current status are same - {}";
}