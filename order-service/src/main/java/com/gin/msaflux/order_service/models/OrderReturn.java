package com.gin.msaflux.order_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_returns")
public class OrderReturn {
    @Id
    private String id;
    private String orderId;
    private List<ReturnItem> items;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private double refundedAmount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReturnItem {
        private String productId;
        private int quantity;
        private String reason;
        private String returnStatus;
    }
}

