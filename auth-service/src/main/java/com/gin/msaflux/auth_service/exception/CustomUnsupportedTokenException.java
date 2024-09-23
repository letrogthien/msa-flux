package com.gin.msaflux.auth_service.exception;

public class CustomUnsupportedTokenException extends RuntimeException {
    public CustomUnsupportedTokenException(String message) {
        super(message);
    }
}
