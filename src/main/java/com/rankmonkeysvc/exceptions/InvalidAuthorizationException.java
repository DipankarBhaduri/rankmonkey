package com.rankmonkeysvc.exceptions;

public class InvalidAuthorizationException extends BaseException {
    public InvalidAuthorizationException(String message){
        super(message);
    }
}