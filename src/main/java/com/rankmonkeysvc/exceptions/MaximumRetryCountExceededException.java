package com.rankmonkeysvc.exceptions;

public class MaximumRetryCountExceededException extends BaseException{
    public MaximumRetryCountExceededException(String message){
        super(message);
    }
}
