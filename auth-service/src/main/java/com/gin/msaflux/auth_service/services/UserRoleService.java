package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.models.UserRole;
import com.gin.msaflux.auth_service.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    public Flux<UserRole> getByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }
}
