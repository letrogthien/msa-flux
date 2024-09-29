package com.gin.msaflux.review_service.services.imp;

import com.gin.msaflux.review_service.models.Review;
import com.gin.msaflux.review_service.models.ReviewShop;
import com.gin.msaflux.review_service.repositories.ReviewRepository;
import com.gin.msaflux.review_service.repositories.ReviewShopRepository;
import com.gin.msaflux.review_service.request.AddReviewRq;
import com.gin.msaflux.review_service.request.AddReviewShopRq;
import com.gin.msaflux.review_service.request.ChangeReviewRq;
import com.gin.msaflux.review_service.request.PageRequestPayload;
import com.gin.msaflux.review_service.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewShopRepository reviewShopRepository;
    @Override
    public Mono<Review> addReviewProduct(AddReviewRq addReviewRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    var userId= securityContext.getAuthentication().getName();
                    return reviewRepository.save(Review.builder()
                            .reviewText(addReviewRq.getReviewText())
                            .createdAt(LocalDateTime.now())
                            .productId(addReviewRq.getProductId())
                            .rating(addReviewRq.getRating())
                            .userId(userId)
                            .build());
                }
        );
    }

    @Override
    public Mono<Review> changeReviewProduct(ChangeReviewRq changeReviewRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    var userId= securityContext.getAuthentication().getName();
                    return reviewRepository.findById(changeReviewRq.getId())
                            .flatMap(
                                    review -> {
                                        if (!review.getUserId().equals(userId)) {
                                            return Mono.error(new BadCredentialsException("you not owner this review"));
                                        }
                                        review.setRating(changeReviewRq.getRating());
                                        review.setReviewText(changeReviewRq.getReviewText());
                                        return reviewRepository.save(review);
                                    }
                            );
                }
        );
    }

    @Override
    public Mono<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Flux<Review> getAllReviewsByProduct(String productId, PageRequestPayload pageRequestPayload) {
        return reviewRepository.findByProductId(productId).skip(pageRequestPayload.getPage() * pageRequestPayload.getSize())
                .take(pageRequestPayload.getSize());
    }

    @Override
    public Flux<ReviewShop> getAllReviewShopByShopId(String shopId, PageRequestPayload pageRequestPayload) {
        return reviewShopRepository.findByShopId(shopId).skip(pageRequestPayload.getPage() * pageRequestPayload.getSize());
    }


    @Override
    public Mono<ReviewShop> addReviewShop(AddReviewShopRq addReviewShopRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    var userId= securityContext.getAuthentication().getName();
                    return reviewShopRepository.save(
                            ReviewShop.builder()
                                    .shopId(addReviewShopRq.getShopId())
                                    .rating(addReviewShopRq.getRating())
                                    .comment(addReviewShopRq.getReviewText())
                                    .userId(userId)
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    );
                }
        );
    }

    @Override
    public Mono<ReviewShop> changeReviewShop(ChangeReviewRq changeReviewRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    var userId= securityContext.getAuthentication().getName();
                    return reviewShopRepository.findById(changeReviewRq.getId())
                            .flatMap(
                                    reviewShop -> {
                                        if (!reviewShop.getUserId().equals(userId)) {
                                            return Mono.error(new BadCredentialsException("you not owner this review"));
                                        }
                                        reviewShop.setRating(changeReviewRq.getRating());
                                        reviewShop.setComment(changeReviewRq.getReviewText());
                                        return reviewShopRepository.save(reviewShop);
                                    }
                            );
                }
        );
    }

    @Override
    public Mono<ReviewShop> getReviewShopById(String id) {
        return reviewShopRepository.findById(id);
    }
}
