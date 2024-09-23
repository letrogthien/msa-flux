package com.gin.msaflux.product_service.services;


import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.request.PageRequestPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> addProduct(ProductDto product) ;
    Mono<Product> updateProduct(ProductDto product) ;
    Mono<Void> deleteProduct(Product product) ;
    Mono<Product> getProduct(String productId) ;
    Flux<Product> getWithPageable(PageRequestPayload pageRequestPayload);

}
