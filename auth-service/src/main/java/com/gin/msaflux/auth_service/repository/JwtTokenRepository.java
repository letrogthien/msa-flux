package com.gin.msaflux.auth_service.repository;

import com.gin.msaflux.auth_service.models.JwtToken;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JwtTokenRepository extends ReactiveMongoRepository<JwtToken, String> {
    Flux<JwtToken> findAllByUserId(String id);
}
