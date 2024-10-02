package com.gin.msaflux.auth_service.controllers;

import com.gin.msaflux.auth_service.dtos.UserDto;
import com.gin.msaflux.auth_service.request.AuthRequest;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.ForgetPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import com.gin.msaflux.auth_service.services.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("login")
    public Mono<AuthResponse> login (@RequestBody final AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("register")
    public Mono<Object> register(@RequestBody final RegisterRq registerRq) {
        return authService.register(registerRq);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("refresh/token")
    public Mono<AuthResponse> refreshToken(ServerHttpRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.getHeaders().getFirst("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .flatMap(authService::refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("password/forget")
    public Mono<String> password(@RequestBody final ForgetPasswordRq forgetPasswordRq) {
        return authService.forgetPassword(forgetPasswordRq.getUserName(), forgetPasswordRq.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("password/change")
    public Mono<String> changePassword(@RequestBody final ChangPasswordRq changPasswordRq) {
        return authService.changePassword(changPasswordRq);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("profile/{id}")
    public Mono<UserDto> getUserProfile(@PathVariable final String id) {
        return authService.getUserById(id);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("profile/update")
    public Mono<Object> updateUserProfile(@RequestBody final UserDto userDto) {
        return authService.updateUser(userDto);
    }

}
