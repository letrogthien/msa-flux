package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
     Mono<AuthResponse> authenticate(String username, String password);
     Mono<AuthResponse> refreshToken();
     Mono<String> forgetPassword(final String userName, final String email);
     Mono<String> changePassword(final ChangPasswordRq changPasswordRq);
     Mono<Object> register(RegisterRq registerRq);

}
