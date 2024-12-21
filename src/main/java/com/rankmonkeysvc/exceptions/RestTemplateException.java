package com.rankmonkeysvc.exceptions;

public class RestTemplateException extends BaseException{
    public RestTemplateException(String message, Exception e) {
        super(message);
    }
}