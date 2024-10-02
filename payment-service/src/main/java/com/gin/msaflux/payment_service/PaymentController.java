package com.gin.msaflux.payment_service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/create")
    public Mono<String> createPayment(ServerHttpRequest serverRequest, @RequestParam int total, @RequestParam String cmt) {
        return paymentService.createOrder(serverRequest,total,cmt);
    }

    @GetMapping("/returnUrl")
    public Mono<Object> returnUrl(ServerHttpRequest serverRequest) {
        return paymentService.returnUrl(serverRequest);
    }
}
