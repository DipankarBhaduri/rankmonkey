package com.rankmonkeysvc.aspects;

import com.rankmonkeysvc.constants.ErrorTitle;
import com.rankmonkeysvc.exceptions.BaseException;
import com.rankmonkeysvc.exceptions.OldAndNewSamePasswordException;
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
public class ProfileControllerAdvice {
    @ExceptionHandler(
            {
                    OldAndNewSamePasswordException.class
            }
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Problem onOldAndNewSamePasswordException (BaseException e) {
        return new Problem()
                .setTitle(ErrorTitle.NEW_AND_OLD_PASSWORD_CANNOT_BE_SAME.toString())
                .setMessage(e.getLocalizedMessage())
                .setStatusCode(HttpStatus.BAD_REQUEST.value());
    }
}
