package com.gin.msaflux.product_service.serviceinterface;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ProductImageServiceInterface {
    Mono<Void> addImagesProduct(Long productId, MultipartFile file) ;
    Mono<Void> deleteImagesProduct(Long productId, Long productImageId) ;
}
