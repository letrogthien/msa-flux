package com.gin.msaflux.auth_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        HttpStatus status;
        String errorMessage;

        if (e instanceof OAuth2AuthenticationException) {
            status = HttpStatus.FORBIDDEN;
            errorMessage = "OAuth2 authentication failed";
        } else {
            status = HttpStatus.UNAUTHORIZED;
            errorMessage = "Authentication failed";
        }

        // Create error details map
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", errorMessage);
        errorDetails.put("timestamp", LocalDateTime.now());

        return writeErrorResponse(exchange, errorDetails, status);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, Map<String, Object> errorDetails, HttpStatus status) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(errorDetails);

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
