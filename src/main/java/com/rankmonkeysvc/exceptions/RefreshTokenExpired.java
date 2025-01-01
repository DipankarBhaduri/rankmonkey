package com.rankmonkeysvc.exceptions;

public class RefreshTokenExpired extends BaseException {
    public RefreshTokenExpired(String message) {
        super(message);
    }
}
