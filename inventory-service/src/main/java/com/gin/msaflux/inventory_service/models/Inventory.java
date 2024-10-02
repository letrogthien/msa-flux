package com.gin.msaflux.inventory_service.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory")
public class Inventory {

    @Id
    private String id;
    private String productId;
    private Long totalCount;
    private Long availableCount;
    private LocalDateTime lastUpdated;

}
