package com.gin.msaflux.review_service.services.imp;

import com.gin.msaflux.review_service.models.Review;
import com.gin.msaflux.review_service.repositories.ReviewRepository;
import com.gin.msaflux.review_service.request.AddReviewRq;
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
public class ServiceReviewImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public Mono<Review> addReview(AddReviewRq addReviewRq) {
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
    public Mono<Review> changeReview(ChangeReviewRq changeReviewRq) {
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
    public Flux<Review> getAllReviewsByProduct(String productId, PageRequestPayload pageRequestPayload) {
        return reviewRepository.findByProductId(productId).skip(pageRequestPayload.getPage() * pageRequestPayload.getSize())
                .take(pageRequestPayload.getSize());
    }
}
