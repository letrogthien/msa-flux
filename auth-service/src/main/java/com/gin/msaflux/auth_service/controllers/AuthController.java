package com.gin.msaflux.auth_service.controllers;

import com.gin.msaflux.auth_service.models.AuthRequest;
import com.gin.msaflux.auth_service.models.AuthResponse;
import com.gin.msaflux.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("login")
    public Mono<AuthResponse> login (@RequestBody final AuthRequest authRequest) {
        return authService.authenticate(authRequest.getUserName(), authRequest.getPassword());
    }
    @PostMapping("refresh")
    public Mono<AuthResponse> refreshToken (ServerWebExchange exchange) {
        return authService.refreshToken(exchange);
    }


}
