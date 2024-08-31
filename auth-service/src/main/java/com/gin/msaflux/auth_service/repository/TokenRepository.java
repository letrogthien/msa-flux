package com.gin.msaflux.auth_service.repository;

import com.gin.msaflux.auth_service.models.Token;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TokenRepository extends ReactiveCrudRepository<Token, String> {
    Flux<Token> findAllByUserId(Long id);
}
