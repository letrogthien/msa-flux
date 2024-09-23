package com.gin.msaflux.common.kafka.payload;

import com.gin.msaflux.common.kafka.status.Status;
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
    private Status status;
}
