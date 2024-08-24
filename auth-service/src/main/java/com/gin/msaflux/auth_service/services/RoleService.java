package com.gin.msaflux.auth_service.services;

import com.gin.msaflux.auth_service.models.Role;
import com.gin.msaflux.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Mono<Role> getById(final Long id) {
        return roleRepository.findById(id);
    }
}
