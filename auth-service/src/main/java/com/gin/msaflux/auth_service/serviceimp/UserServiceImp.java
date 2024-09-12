package com.gin.msaflux.auth_service.serviceimp;

import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImp {
    private final UserRepository userRepository;
    public Flux<User> getAll(){
        return userRepository.findAll();
    }
    public Mono<User> getByUserName(final String name){
        return userRepository.findByUsername(name);
    }
    public Mono<User> getByUserId(final String id){
        return userRepository.findById(id);
    }
    public Mono<User> save(final User user){return userRepository.save(user);}
}
