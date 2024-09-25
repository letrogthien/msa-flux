package com.gin.msaflux.product_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeRq {
    private String productId;
    private String name;
    private String value;
}
