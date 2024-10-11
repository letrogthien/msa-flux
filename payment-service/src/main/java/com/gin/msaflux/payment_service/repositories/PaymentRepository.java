package com.gin.msaflux.payment_service.repositories;

import com.gin.msaflux.payment_service.models.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {
    Mono<Payment> findByOrderId(String s);
}
