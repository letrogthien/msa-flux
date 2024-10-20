package com.gin.msaflux.inventory_service.kafka;



import com.gin.msaflux.common.kafka.payload.AddProduct;
import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.inventory_service.models.Inventory;
import com.gin.msaflux.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    private final KafkaUtils kafkaUtils;
    private final InventoryService inventoryService;

    @RetryableTopic(attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 5000),
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(
            topics = "inventory-check",
            concurrency = "3",
            groupId = "inventory-check-topic-gr1"
    )
    public Mono<Void> forgotPasswordNotify(final String jsonNodeString) {
        return kafkaUtils.jsonNodeToObject(jsonNodeString, OrderPayload.class)
                .flatMap(inventoryService::checkAvailableInventory
        ).then();

    }
    @RetryableTopic(attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 5000),
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(
            topics = "add-product",
            concurrency = "3",
            groupId = "add-product-gr2"
    )
    public Mono<Void> listen(String checkOwnerShop) {
        return kafkaUtils.jsonNodeToObject(checkOwnerShop, AddProduct.class)
                .flatMap(addProduct ->  {
                    Inventory inventory = Inventory.builder()
                            .availableCount(addProduct.getQuantity())
                            .totalCount(addProduct.getQuantity())
                            .productId(addProduct.getProductId())
                            .lastUpdated(LocalDateTime.now())
                            .build();
                    return inventoryService.saveInventory(inventory);
                }).then();
    }


}
