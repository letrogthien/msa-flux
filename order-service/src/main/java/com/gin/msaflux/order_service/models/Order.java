package com.gin.msaflux.order_service.models;

import com.gin.msaflux.common.kafka.status.PaymentMethod;
import com.gin.msaflux.common.kafka.status.Status;
import com.gin.msaflux.order_service.commom.OrderStatus;
import lombok.Builder;
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
@Builder
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private Status status;
    private PaymentMethod paymentType;
    private Address shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItem {
        private String productId;
        private int quantity;
        private double price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Address {
        private String addressLine;
        private String city;
        private String country;
    }
}

