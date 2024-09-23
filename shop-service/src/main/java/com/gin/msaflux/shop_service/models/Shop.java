package com.gin.msaflux.shop_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "shops")
public class Shop {
    @Id
    private String id;
    private String ownerId;
    private String shopName;
    private List<String> categoriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
