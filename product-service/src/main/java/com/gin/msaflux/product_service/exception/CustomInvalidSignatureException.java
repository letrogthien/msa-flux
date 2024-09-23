package com.gin.msaflux.product_service.exception;

import io.jsonwebtoken.SignatureException;

public class CustomInvalidSignatureException extends SignatureException {
    public CustomInvalidSignatureException(String message) {
        super(message);
    }
}
