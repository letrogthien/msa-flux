package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.models.Token;
import com.gin.msaflux.auth_service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Flux<Token> getAllByUserId(Long userId){
        return tokenRepository.findAllByUserId(userId);
    }

    public Flux<Token> getAllChangeRevokedExpired(Long userId){
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

    public Mono<Token> saveToken(String token, Long userId, boolean refreshToken){
        return tokenRepository.save(
                Token.builder()
                        .jwtToken(token)
                        .type("BEARER")
                        .userId(userId)
                        .expired(false)
                        .revoked(false)
                        .refresh(refreshToken)
                        .build()
        );
    }

}
