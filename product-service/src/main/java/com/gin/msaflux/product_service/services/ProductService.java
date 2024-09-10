package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.models.Category;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.models.ProductCategory;
import com.gin.msaflux.product_service.models.ProductImage;
import com.gin.msaflux.product_service.repositories.CategoryRepository;
import com.gin.msaflux.product_service.repositories.ProductCategoryRepository;
import com.gin.msaflux.product_service.repositories.ProductImageRepository;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.ProductRq;
import com.gin.msaflux.product_service.serviceinterface.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public Mono<Product> addProduct(ProductRq productRq) {
        Category category = productRq.getCategory();
        Product product = productRq.getProduct();
        List<String> productImages = productRq.getImagesUrl();

        return productRepository.save(product)
                .flatMap(savedProduct ->
                        categoryRepository.findByCategoryName(category.getCategoryName())
                                .switchIfEmpty(categoryRepository.save(category))

                                .flatMap(savedCategory ->
                                        productCategoryRepository.save(ProductCategory.builder()
                                                        .productId(savedProduct.getId())
                                                        .categoryId(savedCategory.getId())
                                                        .build())
                                                .then(Mono.just(savedProduct))
                                )
                )
                .flatMap(savedProduct -> {

                    List<ProductImage> productImageList = productImages.stream()
                            .map(url -> ProductImage.builder()
                                    .imageUrl(url)
                                    .productId(savedProduct.getId())
                                    .isPrimary(true)
                                    .build())
                            .toList();

                    return productImageRepository.saveAll(Flux.fromIterable(productImageList))
                            .then(Mono.just(savedProduct));
                });
    }

    @Override
    public Mono<Void> updateProduct(ProductRq productRq) {
        return null;
    }

    @Override
    public Mono<Void> deleteProduct(ProductRq productRq) {
        return null;
    }

    @Override
    public Mono<Product> getProduct(Long productId) {
        return null;
    }
}
