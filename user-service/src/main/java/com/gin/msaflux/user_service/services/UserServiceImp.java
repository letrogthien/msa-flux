package com.gin.msaflux.user_service.services;

import com.gin.msaflux.user_service.models.User;
import com.gin.msaflux.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> updateUser(User newUser) {
        return userRepository.findById(newUser.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Something wrong")))
                .flatMap(user -> {
                    user.setUpdatedAt(LocalDateTime.now());
                    user.setAddresses(newUser.getAddresses());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    return userRepository.save(user);
                });
    }
}
