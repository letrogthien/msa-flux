package com.gin.msaflux.auth_service.services;



import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.models.AuthResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil  jwtUtil;
    private final ReactiveAuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    public Mono<AuthResponse> authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken)
                .map(authentication -> {
                    String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
                    String refreshToken=jwtUtil.generateRefreshToken((UserDetails) authentication.getPrincipal());
                    return new AuthResponse(token,refreshToken);
                })
                .onErrorResume(e -> Mono.error(new BadCredentialsException("Invalid username or password")));
    }

    public Mono<AuthResponse> refreshToken(ServerWebExchange exchange) {
        final String refreshToken;
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new BadCredentialsException("Invalid token"));
        }
        refreshToken = authHeader.substring(7);
        try {
            final String userName = jwtUtil.extractUserName(refreshToken);
            return customUserDetailsService.findByUsername(userName)
                    .flatMap(userDetails -> {
                        String newToken = jwtUtil.generateToken(userDetails);
                        return Mono.just(new AuthResponse(newToken, refreshToken));
                    })
                    .onErrorResume(e -> Mono.error(new BadCredentialsException("Invalid refresh token")));
        } catch (Exception e){
            return Mono.error(new BadCredentialsException("Invalid refresh token"));
        }
    }

}
