package com.gin.msaflux.review_service.config;

import com.gin.msaflux.review_service.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class Security {
    private final JwtFilter jwtFilter;
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.authorizeExchange(
                authorizeExchangeSpec ->
                    authorizeExchangeSpec
                            .pathMatchers(HttpMethod.GET).permitAll()
                            .anyExchange().authenticated()

        ).addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
