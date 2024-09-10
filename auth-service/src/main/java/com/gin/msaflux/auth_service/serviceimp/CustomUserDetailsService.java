package com.gin.msaflux.auth_service.serviceimp;


import lombok.RequiredArgsConstructor;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final UserServiceImp userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.getByUserName(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> {
                    List<SimpleGrantedAuthority> authorities =user.getRoles().stream().map(
                            SimpleGrantedAuthority::new
                    ).toList();
                    return Mono.just(User.builder()
                            .authorities(authorities)
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .build());
                });
    }
}
