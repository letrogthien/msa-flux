package com.gin.msaflux.order_service.services;

import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.order_service.models.Order;
import com.gin.msaflux.order_service.request.CreateOrderRequest;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> createOrder(CreateOrderRequest createOrderRequest);
    Mono<Order> rejectingOrder(String orderId);
    Mono<Order> approveOrder(String orderId);
    Mono<Order> updateTotalAmount(OrderPayload orderPayload);
}
