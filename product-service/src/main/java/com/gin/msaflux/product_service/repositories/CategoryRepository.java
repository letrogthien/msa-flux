package com.gin.msaflux.product_service.repositories;

import com.gin.msaflux.product_service.models.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Flux<Category> findByParentCategoryId(String id);
}
