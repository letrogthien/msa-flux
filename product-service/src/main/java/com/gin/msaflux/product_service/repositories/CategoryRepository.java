package com.gin.msaflux.product_service.repositories;

import com.gin.msaflux.product_service.models.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByCategoryName(String name);
}
