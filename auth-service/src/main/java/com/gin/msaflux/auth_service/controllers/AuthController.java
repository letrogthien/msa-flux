package com.gin.msaflux.auth_service.controllers;

import com.gin.msaflux.auth_service.request.AuthRequest;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.ForgetPasswordRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import com.gin.msaflux.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("login")
    public Mono<AuthResponse> login (@RequestBody final AuthRequest authRequest) {
        return authService.authenticate(authRequest.getUserName(), authRequest.getPassword());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("refresh")
    public Mono<AuthResponse> refreshToken() {
        return authService.refreshToken();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("password/forget")
    public Mono<String> password(@RequestBody final ForgetPasswordRq forgetPasswordRq) {
        return authService.forgetPassword(forgetPasswordRq.getUserName(), forgetPasswordRq.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("password/change")
    public Mono<String> changePassword(@RequestBody final ChangPasswordRq changPasswordRq) {
        return authService.changePassword(changPasswordRq.getOldPassword(), changPasswordRq.getNewPassword());
    }

}
