package com.gin.msaflux.product_service.kafka.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddProduct {
    private String userId;
    private String shopId;
    private String productId;
    private boolean accepted;
}
