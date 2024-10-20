package com.gin.msaflux.inventory_service.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "inventory")
public class Inventory {

    @Id
    private String id;
    private String productId;
    private int totalCount;
    private int availableCount;
    private LocalDateTime lastUpdated;

}
