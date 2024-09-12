package com.gin.msaflux.product_service.repositories;

import com.gin.msaflux.product_service.models.ProductImage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends ReactiveMongoRepository<ProductImage, String> {
}
