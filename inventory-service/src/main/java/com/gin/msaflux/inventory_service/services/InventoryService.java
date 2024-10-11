package com.gin.msaflux.inventory_service.services;

import com.gin.msaflux.common.kafka.payload.OrderPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InventoryService {
    Mono<Boolean> decTotalInventory(Long quantity, String productId);
    Mono<Boolean> incTotalInventory(Long quantity,String productId);
    Mono<Boolean> decInventoryAvailable(Long quantity,String productId);
    Mono<Boolean> incInventoryAvailable(Long quantity,String productId);
    Mono<Object> checkAvailableInventory(OrderPayload orderItemPayloads);
}
