package com.gin.msaflux.product_service.controllers;

import com.gin.msaflux.product_service.request.AddProductRq;
import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getAllProducts(
            @RequestParam Long page,
            @RequestParam Long size
    ) {
        return productService.getWithPageable(page,size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Product> getProductsById(
            @PathVariable String id
    ) {
        return productService.getProduct(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Product> addProduct(
            @RequestBody AddProductRq product
    ) {
        return productService.addProduct(product);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Product> updateProduct(
            @RequestBody ProductDto product
    ) {
        return productService.updateProduct(product);
    }


    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteProduct(
            @PathVariable String id
    ) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> searchProducts(
            @RequestParam String searchQuery,
            @RequestParam Long page,
            @RequestParam Long size
    ) {
        return productService.searchProductPageable(page, size, searchQuery);
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> filterProducts(
            @RequestParam Long page,
            @RequestParam Long size,
            @RequestParam String categoryId,
            @RequestParam Long rating,
            @RequestParam BigDecimal price
    ) {
        return productService.getWithFilterPageable(page, size, categoryId, rating, price);
    }
}
