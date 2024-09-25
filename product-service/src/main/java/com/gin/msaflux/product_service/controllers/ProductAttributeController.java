package com.gin.msaflux.product_service.controllers;

import com.gin.msaflux.product_service.models.ProductAttribute;
import com.gin.msaflux.product_service.request.ProductAttributeRq;
import com.gin.msaflux.product_service.services.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v1/product/attribute")
@RequiredArgsConstructor
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductAttribute> addProductAttribute(@RequestBody final ProductAttributeRq productAttribute) {
        return productAttributeService.addProductAttribute(productAttribute);
    }
}
