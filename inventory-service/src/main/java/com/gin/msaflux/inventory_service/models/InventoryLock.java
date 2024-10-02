package com.gin.msaflux.inventory_service.models;


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
@Document(collection = "inventory_locks")
public class InventoryLock {

    @Id
    private String lockId;
    private String itemId;
    private String orderId;
    private Long quantity;
    private String status;
    private LocalDateTime lockedAt;
    private LocalDateTime lastUpdated;

}
