package com.gin.msaflux.user_service.controllers;


import com.gin.msaflux.user_service.models.User;
import com.gin.msaflux.user_service.services.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("api/v1/user")
public class UserController {
    private final UserServiceImp userService;

    @GetMapping("profile/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> profile(@PathVariable String id) {
        return userService.getUserById(id);
    }

}
