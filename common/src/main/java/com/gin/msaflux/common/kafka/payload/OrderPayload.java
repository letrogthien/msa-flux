package com.gin.msaflux.common.kafka.payload;


import com.gin.msaflux.common.kafka.status.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPayload {
    private String orderId;
    private List<OrderItemPayload> orderItems;
    private double totalPrice;
    private PaymentMethod paymentType;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemPayload {
        private String productId;
        private int quantity;
        private double price;
    }
}