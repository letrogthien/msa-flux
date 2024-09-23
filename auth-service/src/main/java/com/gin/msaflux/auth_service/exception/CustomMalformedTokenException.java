package com.gin.msaflux.auth_service.exception;

public class CustomMalformedTokenException extends RuntimeException {
    public CustomMalformedTokenException(String message) {
        super(message);
    }
}
