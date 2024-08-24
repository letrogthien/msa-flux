package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Data
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<User> getAll(){
        return userRepository.findAll();
    }
    public Mono<User> getByUserName(final String name){
        return userRepository.findByUsername(name);
    }
}
