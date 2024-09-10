package com.gin.msaflux.product_service.repositories;

import com.gin.msaflux.product_service.models.ProductCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends ReactiveCrudRepository<ProductCategory, Long> {
}
