package com.gin.msaflux.user_service.repositories;

import com.gin.msaflux.user_service.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    public Mono<User> findByUsername(String username);
}
