package com.gin.msaflux.payment_service.models;

import com.gin.msaflux.common.kafka.status.PaymentMethod;
import com.gin.msaflux.common.kafka.status.Status;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId;
    private PaymentMethod paymentMethod;
    private Status status;
    private String transactionId;
    private double amount;
    private LocalDateTime createdAt;
}
