package com.gin.msaflux.product_service.services.impl;

import com.gin.msaflux.product_service.models.ProductAttribute;
import com.gin.msaflux.product_service.repositories.ProductAttributeRepository;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.ProductAttributeRq;
import com.gin.msaflux.product_service.services.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements ProductAttributeService {
    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;
    @Override
    public Mono<ProductAttribute> addProductAttribute(ProductAttributeRq productAttributeRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext ->
                     productRepository.findById(productAttributeRq.getProductId())
                            .flatMap( product -> {
                                if (!product.getSellerId().equalsIgnoreCase(securityContext.getAuthentication().getName())){
                                    return Mono.error(new BadCredentialsException("unauthorized"));
                                }

                                return productAttributeRepository.save(ProductAttribute.builder()
                                        .productId(product.getId())
                                        .value(productAttributeRq.getValue())
                                        .name(productAttributeRq.getName()).build());
                            })
                
        );
    }
}
