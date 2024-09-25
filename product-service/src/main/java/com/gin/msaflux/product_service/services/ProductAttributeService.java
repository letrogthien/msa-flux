package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.models.ProductAttribute;
import com.gin.msaflux.product_service.request.ProductAttributeRq;
import reactor.core.publisher.Mono;

public interface ProductAttributeService {
    Mono<ProductAttribute> addProductAttribute(ProductAttributeRq productAttributeRq) ;

}
