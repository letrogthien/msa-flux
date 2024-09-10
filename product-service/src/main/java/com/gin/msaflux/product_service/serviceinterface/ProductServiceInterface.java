package com.gin.msaflux.product_service.serviceinterface;

import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.models.ProductImage;
import com.gin.msaflux.product_service.request.ProductRq;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServiceInterface {
    Mono<Product> addProduct(ProductRq productRq) ;
    Mono<Void> updateProduct(ProductRq productRq) ;
    Mono<Void> deleteProduct(ProductRq productRq) ;
    Mono<Product> getProduct(Long productId) ;


}
