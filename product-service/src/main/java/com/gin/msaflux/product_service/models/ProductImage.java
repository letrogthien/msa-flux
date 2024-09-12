package com.gin.msaflux.product_service.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_images")
public class ProductImage {
    @Id
    private String id;
    private String productId;
    private String imageUrl;
    private String altText;
}
