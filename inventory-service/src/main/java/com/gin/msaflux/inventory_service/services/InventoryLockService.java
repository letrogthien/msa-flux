package com.gin.msaflux.inventory_service.services;

import com.gin.msaflux.inventory_service.models.InventoryLock;
import reactor.core.publisher.Mono;

public interface InventoryLockService {
    Mono<InventoryLock> changeStatusToConfirmed(String inventoryLockId);
    Mono<InventoryLock> changeStatusToCancelled(String inventoryLockId);
}
