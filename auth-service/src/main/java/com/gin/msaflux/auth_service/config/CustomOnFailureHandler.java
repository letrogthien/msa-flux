package com.gin.msaflux.auth_service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomOnFailureHandler implements ServerAuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        HttpStatus status;
        String errorMessage;

        if (exception instanceof OAuth2AuthenticationException) {
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
        errorDetails.put("timestamp", LocalDateTime.now().toString());

        return writeErrorResponse(webFilterExchange.getExchange(), errorDetails, status);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, Map<String, Object> errorDetails, HttpStatus status) throws JsonProcessingException {

            String jsonResponse = objectMapper.writeValueAsString(errorDetails);

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(dataBuffer));

    }
}
