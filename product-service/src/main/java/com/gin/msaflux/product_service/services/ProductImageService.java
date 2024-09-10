package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
}
