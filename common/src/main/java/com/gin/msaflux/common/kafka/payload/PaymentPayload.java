package com.gin.msaflux.common.kafka.payload;


import com.gin.msaflux.common.kafka.status.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentPayload {
    private double total;
    private String orderInfo;
    private PaymentMethod paymentType;
}
