package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.dtos.UserDto;
import com.gin.msaflux.auth_service.request.AuthRequest;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
     Mono<AuthResponse> authenticate(AuthRequest authRequest);
     Mono<AuthResponse> refreshToken(String token);
     Mono<String> forgetPassword(final String userName, final String email);
     Mono<String> changePassword(final ChangPasswordRq changPasswordRq);
     Mono<Object> register(RegisterRq registerRq);
     Mono<Object> updateUser(UserDto user);
     Mono<UserDto> getUserById(String userId);

}
