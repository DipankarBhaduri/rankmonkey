package com.rankmonkeysvc.aspects;

import com.rankmonkeysvc.constants.ErrorTitle;
import com.rankmonkeysvc.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(
            {
                    DuplicateEmailException.class
            }
    )
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Problem onDuplicateEmailException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.DUPLICATE_EMAIL.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @ExceptionHandler(
            {
					UserNotFoundException.class
            }
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Problem onUserNotFoundExceptionError (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.USER_NOT_FOUND.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.NOT_FOUND.value());
    }
	
	@ExceptionHandler(
			{
					IncorrectCredentialsException.class
			}
	)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Problem onIncorrectCredentialsExceptionError (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.BAD_CREDENTIALS.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(
			{
					InvalidEmailFormatException.class
			}
	)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Problem onInvalidEmailFormatException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.INVALID_EMAIL_FORMAT.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
	}

	@ExceptionHandler(
			{
					DatabaseOperationException.class,
					IncorrectResultSizeException.class
			}
	)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Problem onDatabaseOperationException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.DATABASE_OPERATION_FAILED.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
	}

	@ExceptionHandler(
			{
					MaximumRetryCountExceededException.class
			}
	)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public Problem onMaximumRetryCountExceededException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.MAXIMUM_RETRY_COUNT_EXCEEDED.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
	}

	@ExceptionHandler(
			{
					LinkExpiredException.class
			}
	)
	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	public Problem onLinkExpiredException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.LINK_HAS_EXPIRED.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.REQUEST_TIMEOUT.value());
	}

	@ExceptionHandler(
			{
					InvalidEmailException.class
			}
	)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Problem onInvalidEmailException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.INVALID_EMAIL.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(
			{
					RefreshTokenExpired.class
			}
	)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Problem onRefreshTokenExpired (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.REFRESH_TOKEN_EXPIRED.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(
			{
					InvalidTokenException.class
			}
	)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Problem onInvalidTokenException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.INVALID_TOKEN.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(
			{
					InvalidAuthTokenException.class
			}
	)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Problem onInvalidAuthTokenException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.INVALID_TOKEN.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(
			{
					InvalidPasswordException.class
			}
	)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Problem onInvalidPasswordException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.INVALID_PASSWORD.toString())
				.setMessage(e.getLocalizedMessage())
				.setStatusCode(HttpStatus.BAD_REQUEST.value());
	}
}