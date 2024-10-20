package com.gin.msaflux.inventory_service.services.impl;

import com.gin.msaflux.common.kafka.payload.CheckInventory;
import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.inventory_service.kafka.KafkaUtils;

import com.gin.msaflux.inventory_service.models.Inventory;
import com.gin.msaflux.inventory_service.repositories.InventoryRepository;
import com.gin.msaflux.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaUtils kafkaUtils;

    @Override
    public Mono<Boolean> decTotalInventory(int quantity, String productId) {
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
    public Mono<Boolean> incTotalInventory(int quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    inventory.setTotalCount(inventory.getTotalCount()+quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }

    @Override
    public Mono<Boolean> decInventoryAvailable(int quantity, String productId) {
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
    public Mono<Boolean> incInventoryAvailable(int quantity, String productId) {
        return inventoryRepository.findByProductId(productId).flatMap(
                inventory -> {
                    inventory.setAvailableCount(inventory.getAvailableCount()+quantity);
                    return inventoryRepository.save(inventory);
                }
        ).then(Mono.just(true));
    }


    @Override
    public Mono<Object> checkAvailableInventory(OrderPayload orderPayload) {
        List<OrderPayload.OrderItemPayload> orderItemPayloads = orderPayload.getOrderItems();
        Flux<String> list = checkPerItem(orderItemPayloads);
        Mono<Boolean> booleanMono = list.all(o -> o.equalsIgnoreCase("00"));
        return booleanMono.flatMap(
                i -> {
                    if (Boolean.FALSE.equals(i)) {
                        CheckInventory checkInventory = CheckInventory.builder()
                                .success(false)
                                .message(orderPayload.getOrderId()).build();
                        return kafkaUtils.sendMessage("inventory-check-failed", checkInventory)
                                .then(Mono.empty());
                    }

                    return kafkaUtils.sendMessage("inventory-check-success", orderPayload);


                }
        );

    }

    @Override
    public Mono<Inventory> saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    /*
    00 : ok
    01 : het hang
    02 : tam het hang
    03 : khong du so luong
     */
    private Flux<String> checkPerItem(List<OrderPayload.OrderItemPayload> orderItemPayloads) {
        return Flux.fromIterable(orderItemPayloads)
                .flatMap(orderItemPayload ->
                        inventoryRepository.findByProductId(orderItemPayload.getProductId())
                                .switchIfEmpty(Mono.error(new RuntimeException("something wrong")))
                                .flatMap(inventory -> {
                                    if (inventory.getTotalCount() == 0) {
                                        return Mono.just("01");
                                    }
                                    if (inventory.getTotalCount() < orderItemPayload.getQuantity()) {
                                        return Mono.just("03");
                                    }
                                    if (inventory.getAvailableCount() < orderItemPayload.getQuantity()) {
                                        return Mono.just("02");
                                    }
                                    return Mono.just("00");
                                })
                );
    }


}
