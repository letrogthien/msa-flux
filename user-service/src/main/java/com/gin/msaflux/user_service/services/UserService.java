package com.gin.msaflux.user_service.services;

import com.gin.msaflux.user_service.dto.UserDto;
import com.gin.msaflux.user_service.models.User;
import reactor.core.publisher.Mono;

public interface UserService {
     Mono<User> getUserById(String id);

     Mono<User> updateUser( UserDto user);
}
