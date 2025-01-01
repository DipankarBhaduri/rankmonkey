package com.rankmonkeysvc.messages;

public class ErrorMessages {
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String INCORRECT_CREDENTIALS = "Invalid username or password.";
    public static final String INCORRECT_OLD_PASSWORD = "Incorrect old password.";
    public static final String EMPTY_EMAIL = "Empty email cannot be allowed.";
    public static final String WRONG_EMAIL_FORMAT = "Invalid email format.";
    public static final String EMAIL_FORMAT_NOT_ALLOWED = "Please use a valid organization email.";
    public static final String DATABASE_OPERATION_FAIL = "Database connection failed.";
    public static final String INCORRECT_RESULT_SIZE_EXCEPTION = "Expected single result but got multiple.";
    public static final String MAXIMUM_RETRY_COUNT_EXCEEDED_MESSAGE = "Maximum retry count exceeded. Please try again after 24 hours.";
    public static final String INVALID_PASSWORD = "Minimum 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character is required.";
    public static final String TOKEN_IS_INVALID_MESSAGE = "Token is invalid.";
    public static final String LINK_HAS_EXPIRED_MESSAGE = "Link has expired.";
    public static final String NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME_MESSAGE = "New password and old password cannot be same.";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired.";
    public static final String USER_DOES_NOT_HAVE_CREATION_ACCESS_MESSAGE = "User does not have creation access.";
    public static final String JOB_ROLE_DUPLICATE_CREATION_NOT_ALLOWED = "Job role already exists.";
    public static final String SKILL_DUPLICATE_CREATION_NOT_ALLOWED = "Skill already exists.";
    public static final String JOB_ROLE_ID_IS_INVALID_MESSAGE = "Job role id is invalid.";
    public static final String CURRENT_STATUS_AND_PREVIOUS_STATUS_IS_SAME = "Previous status and current status are same.";
}