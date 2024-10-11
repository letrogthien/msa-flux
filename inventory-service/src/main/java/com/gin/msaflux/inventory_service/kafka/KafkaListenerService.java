package com.gin.msaflux.inventory_service.kafka;



import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    private final KafkaUtils kafkaUtils;
    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "inventory-check",
            concurrency = "3",
            groupId = "inventory-check-topic-gr1"
    )
    public Mono<Void> forgotPasswordNotify(final String jsonNodeString) {
        System.out.println(jsonNodeString);
        return kafkaUtils.jsonNodeToObject(jsonNodeString, OrderPayload.class)
                .flatMap(inventoryService::checkAvailableInventory
        ).then();

    }


}
