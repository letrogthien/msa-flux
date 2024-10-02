package com.gin.msaflux.inventory_service.services.impl;

import com.gin.msaflux.inventory_service.models.InventoryLock;
import com.gin.msaflux.inventory_service.repositories.InventoryLockRepository;
import com.gin.msaflux.inventory_service.services.InventoryLockService;
import com.gin.msaflux.inventory_service.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryLockServiceImpl implements InventoryLockService {
    private final InventoryLockRepository inventoryLockRepository;
    @Override
    public Mono<InventoryLock> changeStatusToConfirmed(String inventoryLockId) {
        return inventoryLockRepository.findById(inventoryLockId)
                .flatMap(inventoryLock -> {
                    inventoryLock.setStatus(Status.CONFIRMED.toString());
                    return inventoryLockRepository.save(inventoryLock);
                });
    }

    @Override
    public Mono<InventoryLock> changeStatusToCancelled(String inventoryLockId) {
        return inventoryLockRepository.findById(inventoryLockId)
                .flatMap(inventoryLock -> {
                    inventoryLock.setStatus(Status.CANCELLED.toString());
                    return inventoryLockRepository.save(inventoryLock);
                });
    }
}
