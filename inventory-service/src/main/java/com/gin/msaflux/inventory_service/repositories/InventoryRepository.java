package com.gin.msaflux.inventory_service.repositories;

import com.gin.msaflux.inventory_service.models.Inventory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface InventoryRepository extends ReactiveMongoRepository<Inventory, String> {
    Mono<Inventory> findByProductId(String id);
}
