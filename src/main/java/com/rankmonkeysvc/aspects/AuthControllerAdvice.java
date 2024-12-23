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
					   .setMessage(e.getLocalizedMessage());
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
					   .setMessage(e.getLocalizedMessage());
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
					   .setMessage(e.getLocalizedMessage());
	}

	@ExceptionHandler(
			{
					InvalidEmailException.class
			}
	)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Problem onInvalidEmailException (BaseException e) {
		return new Problem()
				.setTitle(ErrorTitle.EMPTY_EMAIL.toString())
				.setMessage(e.getLocalizedMessage());
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
				.setMessage(e.getLocalizedMessage());
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
				.setMessage(e.getLocalizedMessage());
	}
}