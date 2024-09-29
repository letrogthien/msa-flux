package com.gin.msaflux.product_service.repositories.custom;

import com.gin.msaflux.product_service.models.Product;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

public interface CustomProductRepository {
    Flux<Product> getWithFilterPageable( String category, Long rating, BigDecimal price);
    Flux<Product> getWithKeyWordPageable( String query);

}
