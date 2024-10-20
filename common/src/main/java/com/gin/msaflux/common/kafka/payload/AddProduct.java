package com.gin.msaflux.common.kafka.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddProduct {
    private String userId;
    private String shopId;
    private String productId;
    private boolean accepted;
    private int quantity;
    private LocalDateTime sendingTime;
}
