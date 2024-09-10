package com.gin.msaflux.auth_service.controllers;


import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.serviceimp.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {
    private final UserServiceImp userService;
    @PostMapping("/all")
    public Flux<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/home")
    public Mono<String> home() {
        return Mono.just("index");
    }
}
