package com.gin.msaflux.product_service.services.impl;

import com.gin.msaflux.product_service.models.Category;
import com.gin.msaflux.product_service.repositories.CategoryRepository;
import com.gin.msaflux.product_service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Flux<Category> getByParentCategoryId(String id) {
        return categoryRepository.findByParentCategoryId(id);
    }

    @Override
    public Mono<Category> getByCategoryId(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Flux<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Mono<Category> save(Category category) {
        return null;
    }

    @Override
    public Mono<Category> update(Category category) {
        return null;
    }

    @Override
    public Mono<Category> delete(String id) {
        return null;
    }

    @Override
    public Mono<Category> getByName(String name) {
        return null;
    }
}
