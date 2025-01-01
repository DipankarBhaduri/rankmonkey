package com.rankmonkeysvc.exceptions;

public class OldAndNewSamePasswordException extends BaseException{
    public OldAndNewSamePasswordException(String message) {
        super(message);
    }
}
