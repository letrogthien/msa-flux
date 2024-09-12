package com.gin.msaflux.product_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "discounts")
public class Discount {
    @Id
    private String id;
    private String code;
    private String description;
    private int discountPercentage;
    private List<String> productIds;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

}
