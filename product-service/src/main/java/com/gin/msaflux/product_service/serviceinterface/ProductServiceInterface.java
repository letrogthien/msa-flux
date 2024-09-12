package com.gin.msaflux.product_service.serviceinterface;

import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.request.PageRequestPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServiceInterface {
    Mono<Product> addProduct(ProductDto productDto) ;
    Mono<Void> updateProduct(ProductDto productDto) ;
    Mono<Void> deleteProduct(ProductDto productDto) ;
    Mono<Product> getProduct(String productId) ;
    Flux<Product> getWithPageable(PageRequestPayload pageRequestPayload);

}
