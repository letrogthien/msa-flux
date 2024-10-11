package com.gin.msaflux.order_service.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_cancellations")
public class OrderCancellation {
    @Id
    private String id;
    private String orderId;
    private String reason;
    private String cancellationStatus;
    private String cancelledBy;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}

