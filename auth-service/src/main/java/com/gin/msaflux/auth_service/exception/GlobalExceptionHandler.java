package com.gin.msaflux.auth_service.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String TIME_STAMP = "TimeStamp";

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBadCredentialsException(BadCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("BadCredentials");
        return Mono.just(new ResponseEntity<>(problemDetail, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> resourceNotFound(NotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("Resource Not Found");
        return Mono.just(new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleResponseStatusException(ResponseStatusException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("ResponseStatusException");
        return Mono.just(new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler({SignatureException.class, java.security.SignatureException.class})
    public Mono<ResponseEntity<ProblemDetail>> signatureJwt(SignatureException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("JWT Failure");
        return Mono.just(new ResponseEntity<>(problemDetail, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setProperty(TIME_STAMP, LocalDateTime.now());
        problemDetail.setTitle("Internal Server Error");
        return Mono.just(new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
