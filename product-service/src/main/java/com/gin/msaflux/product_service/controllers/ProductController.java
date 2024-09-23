package com.gin.msaflux.product_service.controllers;

import com.gin.msaflux.product_service.dtos.ProductDto;
import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.request.PageRequestPayload;
import com.gin.msaflux.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public Flux<Product> getAllProducts(@RequestBody PageRequestPayload pageRequestPayload) {
        return productService.getWithPageable(pageRequestPayload);
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductsById(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PostMapping("/add")
    public Mono<Product> addProduct(@RequestBody ProductDto product) {
        return productService.addProduct(product);
    }

    @PostMapping("/update")
    public Mono<Product> updateProduct(@RequestBody ProductDto product) {
        return productService.updateProduct(product);
    }
}
