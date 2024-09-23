package com.gin.msaflux.product_service.exception;

public class CustomInvalidTokenException extends IllegalArgumentException {
    public CustomInvalidTokenException(String s) {
        super(s);
    }
}
