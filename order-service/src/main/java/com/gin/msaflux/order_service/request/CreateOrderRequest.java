package com.gin.msaflux.order_service.request;

import com.gin.msaflux.common.kafka.status.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private PaymentMethod paymentType;
    private Address shippingAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private String productId;
        private int quantity;
        private double price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String addressLine;
        private String city; // City
        private String country;
    }
}
