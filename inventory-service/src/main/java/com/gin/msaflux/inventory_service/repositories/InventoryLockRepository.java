package com.gin.msaflux.inventory_service.repositories;

import com.gin.msaflux.inventory_service.models.InventoryLock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InventoryLockRepository extends ReactiveMongoRepository<InventoryLock, String> {
}
