package com.gin.msaflux.auth_service.exception;



public class CustomExpiredTokenException extends RuntimeException  {
    public CustomExpiredTokenException(String message) {
        super(message);
    }
}
