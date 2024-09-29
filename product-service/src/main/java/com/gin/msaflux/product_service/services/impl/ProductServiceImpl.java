package com.gin.msaflux.product_service.services.impl;

import com.gin.msaflux.product_service.common.ApprovalStatus;
import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.kafka.payload.AddProduct;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.repositories.CategoryRepository;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.services.ProductService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    /*
     * adds product to DB and published message to kafka
     * @param product
     * @return Mono<product>
     */
    @Override
    public Mono<Product> addProduct(ProductDto productDto) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext ->
                {
                    Product product= Product.builder()
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .id(null)
                            .sellerId(securityContext.getAuthentication().getName())
                            .name(productDto.getName())
                            .price(productDto.getPrice())
                            .stock(productDto.getStock())
                            .tags(productDto.getTags())
                            .shopId(null)
                            .approvalStatus(ApprovalStatus.PENDING)
                            .description(productDto.getDescription())
                            .categoryId(productDto.getCategoryId())
                            .build();
                    return productRepository.save(product)
                            .flatMap(savedProduct -> {
                                AddProduct checkOwnerShop = AddProduct.builder()
                                        .userId(savedProduct.getId())
                                        .productId(savedProduct.getId())
                                        .build();
                                return Mono.fromRunnable(() -> kafkaTemplate.send("add-product", checkOwnerShop))
                                        .then(Mono.just(savedProduct));
                            });
                }
        );
    }

    @Override
    public Mono<Product> updateProduct(ProductDto product) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productRepository.findById(product.getId())
                .flatMap(
                        p -> {
                            if (!p.getSellerId().equals(userId)) {
                                return Mono.error(
                                        new BadCredentialsException("You are not authorized to update this product")
                                );
                            }
                            return categoryRepository.existsById(p.getCategoryId())
                                    .flatMap(c -> {
                                        if (Boolean.FALSE.equals(c)) {
                                            return Mono.error(new NotFoundException("Category not exists"));
                                        }
                                        return productRepository.save(changePro(product, p));
                                    });
                        }
                );
    }

    /*
    *
     */
    @Override
    public Mono<Void> deleteProduct(String productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productRepository.findById(productId)
                .flatMap(p-> {
                    if (!p.getSellerId().equals(userId)) {
                        return Mono.error(new BadCredentialsException("You are not authorized to delete this product"));
                    }
                    return productRepository.delete(p);
                }).then();
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> getWithPageable(Long page, Long size) {
        return productRepository.findAll().skip(page * size)
                .take(size);
    }

    @Override
    public Flux<Product> getWithFilterPageable(Long page, Long size, String category, Long rating, BigDecimal price) {
        return productRepository.getWithFilterPageable(category, rating, price).skip(page * size).take(size);
    }

    @Override
    public Flux<Product> searchProductPageable(Long page, Long size, String searchQuery) {
        return productRepository.getWithKeyWordPageable(searchQuery).skip(page * size).take(size);
    }


    private Product changePro(ProductDto root, Product productChange) {
        if (productChange.getName() != null) {
            root.setName(productChange.getName());
        }
        if (productChange.getDescription() != null) {
            root.setDescription(productChange.getDescription());
        }
        if (productChange.getCategoryId() != null) {
            root.setCategoryId(productChange.getCategoryId());
        }
        if (productChange.getPrice() != null) {
            root.setPrice(productChange.getPrice());
        }

        if (productChange.getStock() != null) {
            root.setStock(productChange.getStock());
        }
        if (productChange.getTags() != null) {
            root.setTags(productChange.getTags());
        }
        if (productChange.getApprovalStatus() != null) {
            root.setApprovalStatus(productChange.getApprovalStatus());
        }
        root.setUpdatedAt(LocalDateTime.now());

        return productChange;

    }

}
