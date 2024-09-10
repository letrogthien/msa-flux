package com.gin.msaflux.product_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;

}
