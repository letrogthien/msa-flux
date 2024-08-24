package com.gin.msaflux.auth_service.controllers;


import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {
    private final UserService userService;
    @PostMapping("/all")
    public Flux<User> getAll() {
        return userService.getAll();
    }
}
