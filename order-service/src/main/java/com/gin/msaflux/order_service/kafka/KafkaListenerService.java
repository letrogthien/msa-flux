package com.gin.msaflux.order_service.kafka;



import com.gin.msaflux.common.kafka.payload.CheckInventory;
import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.order_service.services.OrderService;
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
    private final OrderService orderService;

    @KafkaListener(
            topics = "inventory-check-failed",
            concurrency = "3",
            groupId = "order-created-topic-gr1"
    )
    Mono<Void> checkInventoryFailed(String checkInventory){
        return kafkaUtils.jsonNodeToObject(checkInventory, CheckInventory.class)
                .flatMap(obj-> orderService.rejectingOrder(obj.getMessage())
                ).then();
    }

    @KafkaListener(
            topics = "order-approved-notify",
            concurrency = "3",
            groupId = "order-approved-notify-topic-gr1"
    )

    Mono<Void> approveOrder(String orderId){

        return kafkaUtils.jsonNodeToObject(orderId, OrderPayload.class)
                .flatMap(obj-> orderService.approveOrder(obj.getOrderId())
                ).then();
    }

    @KafkaListener(
            topics = "inventory-check-success",
            concurrency = "3",
            groupId = "inventory-check-success-topic-gr1"
    )

    Mono<Void> checkInventorySuccess(String checkInventory){
        return kafkaUtils.jsonNodeToObject(checkInventory, OrderPayload.class)
                .flatMap(orderService::updateTotalAmount).then();

    }





}
