package com.gin.msaflux.product_service.repositories;

import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.repositories.custom.CustomProductRepository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String>, CustomProductRepository {
}
