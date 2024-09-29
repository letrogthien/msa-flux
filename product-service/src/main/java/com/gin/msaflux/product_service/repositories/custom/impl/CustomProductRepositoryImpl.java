package com.gin.msaflux.product_service.repositories.custom.impl;

import com.gin.msaflux.product_service.models.Product;
import com.gin.msaflux.product_service.repositories.custom.CustomProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {
    @Override
    public Flux<Product> getWithKeyWordPageable(String searchTerm) {
        Query query = new Query();
        if (searchTerm == null || searchTerm.isEmpty()) {
            return Flux.empty();
        }
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("name").regex(searchTerm,"i"),
                Criteria.where("description").regex(searchTerm,"i")
        );
        query.addCriteria(criteria);
        query.collation(Collation.of("vi").strength(Collation.ComparisonLevel.secondary()));
        return reactiveMongoTemplate.find(query, Product.class);
    }

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Flux<Product> getWithFilterPageable( String category, Long rating, BigDecimal price) {
        Query query = new Query();
        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("categoryId").is(category));
        }
        if (rating != null) {
            query.addCriteria(Criteria.where("rating").gte(rating));
        }
        if (price != null) {
            query.addCriteria(Criteria.where("price").lte(price));
        }
        return reactiveMongoTemplate.find(query, Product.class);
    }
}
