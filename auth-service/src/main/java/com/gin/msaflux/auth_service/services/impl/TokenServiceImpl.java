package com.gin.msaflux.auth_service.services.impl;

import com.gin.msaflux.auth_service.common.TokenType;
import com.gin.msaflux.auth_service.models.JwtToken;
import com.gin.msaflux.auth_service.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {
    private final JwtTokenRepository tokenRepository;



    public Flux<JwtToken> getAllChangeRevokedExpired(String userId){
         return tokenRepository.findAllByUserId(userId)
                 .filter(token -> !token.isExpired() && !token.isRevoked())
                .flatMap(
                        token -> {
                            token.setRevoked(true);
                            token.setExpired(true);
                            return tokenRepository.save(token);
                        }
                );
    }

    public Mono<JwtToken> saveToken(String token, String userId, boolean refreshToken){
        return tokenRepository.save(
                JwtToken.builder()
                        .token(token)
                        .type(TokenType.BEARER)
                        .userId(userId)
                        .expired(false)
                        .revoked(false)
                        .refresh(refreshToken)
                        .build()
        );
    }

}
