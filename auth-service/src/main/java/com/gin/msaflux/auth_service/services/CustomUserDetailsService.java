package com.gin.msaflux.auth_service.services;


import lombok.RequiredArgsConstructor;



import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.getByUserName(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> userRoleService.getByUserId(user.getId())
                        .flatMap(userRole -> roleService.getById(userRole.getRoleId())
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                        )
                        .collectList()
                        .map(authorities -> User.withUsername(user.getUsername())
                                .password(user.getPassword())
                                .authorities(authorities)
                                .build()
                        )
                );
    }
}
