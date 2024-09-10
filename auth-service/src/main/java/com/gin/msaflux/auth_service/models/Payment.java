package com.gin.msaflux.auth_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId; // Reference to the order
    private String paymentMethod; // credit_card | paypal | bank_transfer
    private String status; // pending | completed | failed
    private String transactionId;
    private double amount;
    private LocalDateTime createdAt;
}
