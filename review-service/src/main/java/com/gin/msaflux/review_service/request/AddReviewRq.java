package com.gin.msaflux.review_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AddReviewRq {

    private String userId;

    private String productId;

    private int rating;

    private String reviewText;

}
