package com.gin.msaflux.user_service.comon;

import com.gin.msaflux.user_service.dto.UserDto;
import com.gin.msaflux.user_service.models.User;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class Convert {
    public Mono<User> convertToUser(Mono<User> user, UserDto userDto) {
        return user.flatMap(u -> {
            u.setAddresses(userDto.getAddresses());
            u.setPhoneNumber(userDto.getPhoneNumber());
            u.setUpdatedAt(LocalDateTime.now());
            return Mono.just(u);
        });
    }
}
