package com.gin.msaflux.review_service.repositories;

import com.gin.msaflux.review_service.models.ReviewShop;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewShopRepository extends ReactiveMongoRepository<ReviewShop, String> {
    Flux<ReviewShop> findByShopId(String shopId);
}
