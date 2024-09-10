package com.gin.msaflux.auth_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "discounts")
public class Discount {
    @Id
    private String id;
    private String code;
    private String description;
    private int discountPercentage;
    private LocalDateTime expiryDate;
    private List<AppliesTo> appliesTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppliesTo {
        private String productId; 
        private String categoryId;
    }
}
