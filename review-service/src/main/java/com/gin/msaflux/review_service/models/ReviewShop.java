package com.gin.msaflux.review_service.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews-shop")
public class ReviewShop {
    @Id
    private String id;
    private String shopId;
    private String userId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
