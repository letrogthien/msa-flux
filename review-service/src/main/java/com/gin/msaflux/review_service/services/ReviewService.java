package com.gin.msaflux.review_service.services;

import com.gin.msaflux.review_service.models.Review;

import com.gin.msaflux.review_service.models.ReviewShop;
import com.gin.msaflux.review_service.request.AddReviewRq;
import com.gin.msaflux.review_service.request.AddReviewShopRq;
import com.gin.msaflux.review_service.request.ChangeReviewRq;

import com.gin.msaflux.review_service.request.PageRequestPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
    // review for product
    Mono<Review> addReviewProduct(AddReviewRq addReviewRq);
    Mono<Review> changeReviewProduct(ChangeReviewRq changeReviewRq);
    Mono<Review> getReviewById(String id);
    Flux<Review> getAllReviewsByProduct(String productId, PageRequestPayload pageRequestPayload);

    //review for shop
    Flux<ReviewShop> getAllReviewShopByShopId(String shopId, PageRequestPayload pageRequestPayload);
    Mono<ReviewShop> addReviewShop(AddReviewShopRq addReviewRq);
    Mono<ReviewShop> changeReviewShop(ChangeReviewRq changeReviewRq);
    Mono<ReviewShop> getReviewShopById(String id);
}
