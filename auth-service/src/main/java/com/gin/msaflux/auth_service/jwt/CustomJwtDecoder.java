package com.gin.msaflux.auth_service.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;


@Component
public class CustomJwtDecoder implements ReactiveJwtDecoder {
    @Value("${security.secret.key}")
    private String secretKey;

    @SneakyThrows
    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        JWT jwt = JWTParser.parse(token);
        if (jwt.getJWTClaimsSet().getIssuer()!=null){
            String issuer= jwt.getJWTClaimsSet().getIssuer();
            if (issuer.contains("google")){
                return googleJwtDecoder().decode(token);
            }
        }
        return jwtDecoder().decode(token);
    }




    private ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    private ReactiveJwtDecoder googleJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
}
