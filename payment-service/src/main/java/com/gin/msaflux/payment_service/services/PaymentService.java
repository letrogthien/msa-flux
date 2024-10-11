package com.gin.msaflux.payment_service.services;

import com.gin.msaflux.common.kafka.payload.PaymentPayload;
import com.gin.msaflux.payment_service.models.Payment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Void> paymentMethod(PaymentPayload paymentPayload);
    Mono<Object> returnUrl(ServerHttpRequest request);
    Mono<String> createOrder(ServerHttpRequest serverRequest, double total, String orderInfo);
    Mono<Payment> getByOrderId(String id);
}
