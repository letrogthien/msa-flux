package com.gin.msaflux.review_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ChangeReviewRq {

    private String id;

    private int rating;

    private String reviewText;
}
