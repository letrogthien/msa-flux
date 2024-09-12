package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.jwt.JwtUtil;
import com.gin.msaflux.product_service.models.Product;

import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.PageRequestPayload;
import com.gin.msaflux.product_service.serviceinterface.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {
    private final ProductRepository productRepository;

    @Override
    public Mono<Product> addProduct(ProductDto productDto) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    Product product= Product.builder()
                            .price(productDto.getPrice())
                            .name(productDto.getName())
                            .stock(productDto.getStock())
                            .images(productDto.getImages())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .description(productDto.getDescription())
                            .sellerId(securityContext.getAuthentication().getName())
                            .categoryId(productDto.getCategoryId())
                            .tags(productDto.getTags())
                            .build();
                    return productRepository.save(product);
                }
        );
    }

    @Override
    public Mono<Void> updateProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<Void> deleteProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> getWithPageable(PageRequestPayload pageRequestPayload) {
        return productRepository.findAll().skip(pageRequestPayload.getPage() * pageRequestPayload.getSize())
                .take(pageRequestPayload.getSize());
    }

}
