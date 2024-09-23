package com.gin.msaflux.shop_service.repositories;

import com.gin.msaflux.shop_service.models.Shop;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ShopRepository extends ReactiveMongoRepository<Shop, String> {
    Mono<Shop> findByOwnerId(String s);
}
