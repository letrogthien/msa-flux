package com.gin.msaflux.auth_service.exception;

public class CustomInvalidTokenException extends IllegalArgumentException {
    public CustomInvalidTokenException(String s) {
        super(s);
    }
}
