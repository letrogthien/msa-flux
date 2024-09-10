package com.gin.msaflux.auth_service.jwt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final List<String> nonAuthenticate = List.of("/auth/login", "/auth/register");
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        boolean isNonAuthPath = nonAuthenticate.stream().anyMatch(path::contains);
        if (isNonAuthPath) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        final String token = authHeader.substring(7);

        try {
            if (jwtUtil.isExpiration(token)) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired JWT token"));
            }
            if (!jwtUtil.isTokenValid(token)) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT Token"));
            }
            Set<SimpleGrantedAuthority> authorities = jwtUtil.extractAuthorities(token).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(jwtUtil.extractUserName(token), null, authorities);
            SecurityContext securityContext = new SecurityContextImpl(usernamePasswordAuthenticationToken);
            return chain.filter(exchange).
                    contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
        } catch (Exception e) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Authentication Failed"));
        }
    }
}
