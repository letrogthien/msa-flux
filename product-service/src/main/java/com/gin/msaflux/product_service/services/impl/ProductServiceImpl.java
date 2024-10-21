package com.gin.msaflux.product_service.services.impl;

import com.gin.msaflux.common.kafka.payload.AddProduct;
import com.gin.msaflux.product_service.common.ApprovalStatus;
import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.kafka.KafkaUtils;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.repositories.CategoryRepository;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.AddProductRq;
import com.gin.msaflux.product_service.services.ProductService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
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

    private final KafkaUtils kafkaUtils;
    /*
     * adds product to DB and published message to kafka
     * @param product
     * @return Mono<product>
     */@Override
    public Mono<Product> addProduct(AddProductRq productDto) {
        // Get the current user from the security context
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication().getName())
                .flatMap(sellerId -> validateAndCreateProduct(sellerId, productDto)) // Extracted product creation logic
                .flatMap(product -> saveProduct(product, productDto)); // Handling saving and Kafka message
    }

    @Override
    public Mono<Double> caculateAmount(Flux<String> productIds) {
        return productIds
                .flatMap(productRepository::findById)
                .map(Product::getPrice)
                .reduce(Double::sum);
    }

    // Method to validate the category and build the product
    private Mono<Product> validateAndCreateProduct(String sellerId, AddProductRq productDto) {
        return categoryRepository.findById(productDto.getCategoryId())
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .map(category -> Product.builder()
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .id(null)  // Let the DB assign an ID
                        .sellerId(sellerId)
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .tags(productDto.getTags())
                        .shopId(null)  // May want to set this when available
                        .approvalStatus(ApprovalStatus.PENDING)
                        .description(productDto.getDescription())
                        .categoryId(productDto.getCategoryId())
                        .build()
                );
    }

    // Method to save the product and send Kafka message
    private Mono<Product> saveProduct(Product product, AddProductRq productDto) {
        return productRepository.save(product)
                .flatMap(savedProduct -> sendAddProductEvent(savedProduct, productDto.getStock().getQuantity()));
    }

    // Method to send Kafka message and return the saved product
    private Mono<Product> sendAddProductEvent(Product savedProduct, int quantity) {
        AddProduct addProduct = AddProduct.builder()
                .userId(savedProduct.getSellerId())
                .productId(savedProduct.getId())
                .quantity(quantity)
                .sendingTime(LocalDateTime.now())
                .build();

        // Send Kafka message and return the saved product
        return kafkaUtils.sendMessage("add-product", addProduct)
                .then(Mono.just(savedProduct));
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

        root.setPrice(productChange.getPrice());

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
