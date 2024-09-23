package com.gin.msaflux.auth_service.exception;

import io.jsonwebtoken.SignatureException;

public class CustomInvalidSignatureException extends SignatureException {
    public CustomInvalidSignatureException(String message) {
        super(message);
    }
}
