package com.rankmonkeysvc.exceptions;

public class DuplicateEmailException extends BaseException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
