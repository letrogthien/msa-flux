package com.gin.msaflux.auth_service.config;

import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.services.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return customUserDetailsService.findByUsername(authentication.getName())
                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                        return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                    } else {
                        return Mono.error(new BadCredentialsException("Invalid username or password"));
                    }
                });

    }
}
