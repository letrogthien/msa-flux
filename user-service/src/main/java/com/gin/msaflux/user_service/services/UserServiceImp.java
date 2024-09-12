package com.gin.msaflux.user_service.services;

import com.gin.msaflux.user_service.dto.UserDto;
import com.gin.msaflux.user_service.models.User;
import com.gin.msaflux.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
    public Mono<User> updateUser(UserDto newUser) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> userRepository.findById(securityContext.getAuthentication().getName())
                        .switchIfEmpty(Mono.error(new RuntimeException("Something wrong")))
                        .flatMap(user -> {
                            if (!user.getUsername().equals(newUser.getUsername())) {
                                return Mono.error(new RuntimeException("Username mismatch"));
                            }
                            user.setUpdatedAt(LocalDateTime.now());
                            user.setAddresses(newUser.getAddresses());
                            user.setPhoneNumber(newUser.getPhoneNumber());
                            return userRepository.save(user);
                        })
        );
    }
}
