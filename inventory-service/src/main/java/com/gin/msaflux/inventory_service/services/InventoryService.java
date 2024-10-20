package com.gin.msaflux.inventory_service.services;

import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.inventory_service.models.Inventory;

import reactor.core.publisher.Mono;



public interface InventoryService {
    Mono<Boolean> decTotalInventory(int quantity, String productId);
    Mono<Boolean> incTotalInventory(int quantity,String productId);
    Mono<Boolean> decInventoryAvailable(int quantity,String productId);
    Mono<Boolean> incInventoryAvailable(int quantity,String productId);
    Mono<Object> checkAvailableInventory(OrderPayload orderItemPayloads);
    Mono<Inventory> saveInventory(Inventory inventory);
}
