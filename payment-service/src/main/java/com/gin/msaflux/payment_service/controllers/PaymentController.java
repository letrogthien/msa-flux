package com.gin.msaflux.payment_service.controllers;


import com.gin.msaflux.payment_service.services.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    @PostMapping("/create")
    public Mono<String> createPayment(ServerHttpRequest serverRequest, @RequestParam double total, @RequestParam String cmt) {
        return paymentService.createOrder(serverRequest,total,cmt);
    }

    @GetMapping("/returnUrl")
    public Mono<Object> returnUrl(ServerHttpRequest serverRequest) {
        return paymentService.returnUrl(serverRequest);
    }

    @PostMapping("/pay")
    public Mono<String> pay(ServerHttpRequest serverRequest, @RequestParam String orderId) {
        return paymentService.getByOrderId(orderId)
                .flatMap(o->
                     paymentService.createOrder(serverRequest, o.getAmount(), o.getOrderId())
                );
    }
}
