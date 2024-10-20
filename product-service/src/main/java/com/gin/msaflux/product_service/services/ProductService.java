package com.gin.msaflux.product_service.services;


import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.request.AddProductRq;
import com.gin.msaflux.product_service.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ProductService {
    Mono<Product> addProduct(AddProductRq product) ;
    Mono<Product> updateProduct(ProductDto product) ;
    Mono<Void> deleteProduct(String productId) ;
    Mono<Product> getProduct(String productId) ;
    Flux<Product> getWithPageable(Long page, Long size);
    Flux<Product> getWithFilterPageable(Long page, Long size, String category, Long rating, BigDecimal price);
    Flux<Product> searchProductPageable(Long page, Long size,String searchQuery) ;

}
