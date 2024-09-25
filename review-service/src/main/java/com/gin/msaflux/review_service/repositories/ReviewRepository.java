package com.gin.msaflux.review_service.repositories;

import com.gin.msaflux.review_service.models.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
    Flux<Review> findByProductId(String productId);
}
