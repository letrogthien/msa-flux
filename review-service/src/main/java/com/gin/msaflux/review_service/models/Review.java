package com.gin.msaflux.review_service.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private String userId;

    private String productId;

    private int rating;

    private String reviewText;

    private LocalDateTime createdAt;
}
