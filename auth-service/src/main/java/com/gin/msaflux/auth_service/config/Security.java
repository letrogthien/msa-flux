package com.gin.msaflux.auth_service.config;
import com.gin.msaflux.auth_service.common.Const;
import com.gin.msaflux.auth_service.jwt.CustomJwtDecoder;
import com.gin.msaflux.auth_service.services.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import java.util.List;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class Security {
    private final CustomJwtDecoder jwtDecoder;
    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.oauth2ResourceServer(
                exchange -> exchange.jwt(
                    jwtSpec -> jwtSpec.jwtDecoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );
        http.authorizeExchange(exchange ->
            exchange
                    .pathMatchers("test/all").hasAuthority("CUSTOMER")
                    .pathMatchers(Const.AUTH_PART_LOGIN).permitAll()
                    .pathMatchers(Const.AUTH_PART_REGISTER).permitAll()
                    .pathMatchers(Const.AUTH_PART_FORGET_PASSWORD).permitAll()
                    .anyExchange().authenticated()
            );

        return http.build();
    }





    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {

        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);

        return converter;
    }

    private Flux<GrantedAuthority> extractAuthorities(Jwt jwt) {

        if (jwt.getClaim("iss")!=null && jwt.getIssuer().toString().equals("https://accounts.google.com")) {
            return extractAuthoritiesFromGGToken(jwt);
        }
        return extractAuthoritiesFromToken(jwt);
    }


    private Flux<GrantedAuthority> extractAuthoritiesFromToken(Jwt jwt) {
        if (jwt.getClaim("roles")!=null){
            List<String> roles= jwt.getClaim("roles");
            return Flux.fromIterable(roles)
                    .map(SimpleGrantedAuthority::new);
        }
        return Flux.empty();
    }

    private Flux<GrantedAuthority> extractAuthoritiesFromGGToken(Jwt jwt) {
        String email = jwt.getClaim("email");
        return customUserDetailsService.findByEmail(email)
                .flatMapMany(userDetails -> Flux.fromIterable(userDetails.getAuthorities()));
    }








}
