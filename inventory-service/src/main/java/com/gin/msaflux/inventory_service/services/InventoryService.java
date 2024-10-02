package com.gin.msaflux.inventory_service.services;

import reactor.core.publisher.Mono;

public interface InventoryService {
    Mono<Boolean> decTotalInventory(Long quantity, String productId);
    Mono<Boolean> incTotalInventory(Long quantity,String productId);
    Mono<Boolean> decInventoryAvailable(Long quantity,String productId);
    Mono<Boolean> incInventoryAvailable(Long quantity,String productId);
}
