package com.gin.msaflux.product_service.exception;


import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String TIME_STAMP = "TimeStamp";
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("BadCredentials");
        return new ResponseEntity<>(problemDetail, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> resourceNotFound(NotFoundException e){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("Exception Handle");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("Exception Handle");
        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
