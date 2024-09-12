package com.gin.msaflux.product_service.controllers;

import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.PageRequestPayload;
import com.gin.msaflux.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("all")
    public Flux<Product> getAllProducts(@RequestBody PageRequestPayload payload) {
        return productService.getWithPageable(payload);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Mono<Product> getProductsById(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
