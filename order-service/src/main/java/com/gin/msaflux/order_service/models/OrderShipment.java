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
@Document(collection = "order_shipments")
public class OrderShipment {
    @Id
    private String id;
    private String orderId;
    private String shipmentStatus;
    private String carrier;
    private String trackingNumber;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}

