package com.gin.msaflux.auth_service.config;

import com.gin.msaflux.auth_service.common.Const;
import com.gin.msaflux.auth_service.common.RoleType;
import com.gin.msaflux.auth_service.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class Security {
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange(exchange ->
            exchange
                    .pathMatchers(HttpMethod.GET).permitAll()
                    .pathMatchers(Const.AUTH_PART_LOGIN).permitAll()
                    .pathMatchers(Const.AUTH_PART_REGISTER).permitAll()
                    .pathMatchers(Const.AUTH_PART_FORGET_PASSWORD).permitAll()
                    .anyExchange().authenticated()
            );

        return http.build();
    }

}
