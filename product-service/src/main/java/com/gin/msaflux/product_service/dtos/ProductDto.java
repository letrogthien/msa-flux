package com.gin.msaflux.product_service.dtos;

import com.gin.msaflux.product_service.common.ApprovalStatus;
import com.gin.msaflux.product_service.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private BigDecimal price;
    private Product.Stock stock;
    private String sellerId;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ApprovalStatus approvalStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Stock {
        private int quantity;
        private String warehouseLocation;
    }
}
