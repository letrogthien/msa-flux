package com.gin.msaflux.product_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory_logs")
public class InventoryLog {
    @Id
    private String id;
    private String productId;
    private int quantity;
    private String action;
    private String warehouseLocation;
    private String timestamp;

}
