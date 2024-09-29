package com.gin.msaflux.review_service.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AddReviewShopRq {

    private String userId;

    private String shopId;

    private int rating;

    private String reviewText;

}

