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
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private double discountPrice;
    private String categoryId;
    private String shopId;
    private int inventoryCount;
    private Attributes attributes;
    private List<Image> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attributes {
        private String color;
        private String size;
        private double weight;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {
        private String url;
        private boolean isPrimary;
    }
}
