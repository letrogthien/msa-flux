package com.gin.msaflux.payment_service.kafka;


import com.gin.msaflux.common.kafka.payload.PaymentPayload;
import com.gin.msaflux.payment_service.services.impl.PaymentServiceImpl;
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
    private final PaymentServiceImpl paymentService;

    @KafkaListener(
            topics = "payment-check",
            concurrency = "3",
            groupId = "payment-check-gr-1"
    )
    public Mono<Void> paymentCheck(String paymentPayload){
         return kafkaUtils.jsonNodeToObject(paymentPayload, PaymentPayload.class)
                 .flatMap(paymentService::paymentMethod).then();
    }


}
