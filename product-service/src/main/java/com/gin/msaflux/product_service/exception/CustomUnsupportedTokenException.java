package com.gin.msaflux.product_service.exception;

public class CustomUnsupportedTokenException extends RuntimeException {
    public CustomUnsupportedTokenException(String message) {
        super(message);
    }
}
