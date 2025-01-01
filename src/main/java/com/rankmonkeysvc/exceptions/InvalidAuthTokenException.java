package com.rankmonkeysvc.exceptions;

public class InvalidAuthTokenException extends BaseException{
    public InvalidAuthTokenException(String message){
        super(message);
    }
}
