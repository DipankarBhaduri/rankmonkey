package com.rankmonkeysvc.aspects;

import com.rankmonkeysvc.constants.ErrorTitle;
import com.rankmonkeysvc.exceptions.BaseException;
import com.rankmonkeysvc.exceptions.DuplicateRecordException;
import com.rankmonkeysvc.exceptions.InvalidAuthorizationException;
import com.rankmonkeysvc.exceptions.InvalidRequestException;
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
public class JobRoleControllerAdvice {

    @ExceptionHandler(
            {
                    InvalidAuthorizationException.class
            }
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Problem onInvalidAuthorizationException (BaseException e) {
        return new Problem()
                .setTitle(ErrorTitle.UNAUTHORIZED_ENDPOINT.toString())
                .setMessage(e.getLocalizedMessage())
                .setStatusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(
            {
                    DuplicateRecordException.class
            }
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    public Problem onDuplicateRecordException (BaseException e) {
        return new Problem()
                .setTitle(ErrorTitle.DUPLICATE_JOB_ROLE.toString())
                .setMessage(e.getLocalizedMessage())
                .setStatusCode(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(
            {
                    InvalidRequestException.class
            }
    )
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Problem onInvalidRequestException (BaseException e) {
        return new Problem()
                .setTitle(ErrorTitle.INVALID_REQUEST.toString())
                .setMessage(e.getLocalizedMessage())
                .setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
    }
}
