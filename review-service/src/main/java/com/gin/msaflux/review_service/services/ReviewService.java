package com.gin.msaflux.review_service.services;

import com.gin.msaflux.review_service.models.Review;

import com.gin.msaflux.review_service.request.AddReviewRq;
import com.gin.msaflux.review_service.request.ChangeReviewRq;

import com.gin.msaflux.review_service.request.PageRequestPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
    Mono<Review> addReview(AddReviewRq addReviewRq);
    Mono<Review> changeReview(ChangeReviewRq changeReviewRq);
    Flux<Review> getAllReviewsByProduct(String productId, PageRequestPayload pageRequestPayload);
}
