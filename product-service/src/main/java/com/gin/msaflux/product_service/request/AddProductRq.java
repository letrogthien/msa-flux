package com.gin.msaflux.product_service.request;

import com.gin.msaflux.product_service.common.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductRq {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private double price;
    private Stock stock;
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
