package com.gin.msaflux.order_service.controller;

import com.gin.msaflux.order_service.models.Order;
import com.gin.msaflux.order_service.request.CreateOrderRequest;
import com.gin.msaflux.order_service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public Mono<Order> create(@RequestBody CreateOrderRequest order) {
        return orderService.createOrder(order);
    }

}
