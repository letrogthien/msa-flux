package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.models.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Flux<Category> getByParentCategoryId(String id);
    Mono<Category> getByCategoryId(String id);
    Flux<Category> getAll();
    Mono<Category> save(Category category);
    Mono<Category> update(Category category);
    Mono<Category> delete(String id);
    Mono<Category> getByName(String name);

}
