package com.gin.msaflux.product_service.repositories;


import com.gin.msaflux.product_service.models.ProductAttribute;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends ReactiveMongoRepository<ProductAttribute, String> {
}
