package com.gin.msaflux.inventory_service.services.impl;

import com.gin.msaflux.inventory_service.repositories.InventoryRepository;
import com.gin.msaflux.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ReactiveMongoTemplate mongoTemplate;
    private final InventoryRepository inventoryRepository;

    @Override
    public Mono<Boolean> decTotalInventory(Long quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    if (inventory.getTotalCount()<quantity) {
                        return Mono.just(false);
                    }
                    inventory.setTotalCount(inventory.getTotalCount()-quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }

    @Override
    public Mono<Boolean> incTotalInventory(Long quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    inventory.setTotalCount(inventory.getTotalCount()+quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }

    @Override
    public Mono<Boolean> decInventoryAvailable(Long quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    if (inventory.getAvailableCount()<quantity) {
                        return Mono.just(false);
                    }
                    inventory.setAvailableCount(inventory.getAvailableCount()-quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }

    @Override
    public Mono<Boolean> incInventoryAvailable(Long quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    inventory.setAvailableCount(inventory.getAvailableCount()+quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }
}
