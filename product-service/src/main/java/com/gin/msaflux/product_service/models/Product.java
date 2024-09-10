package com.gin.msaflux.product_service.models;


import com.gin.msaflux.product_service.common.ApprovalStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("products")
public class Product {

    @Id
    private Long id;

    @Column("shop_id")
    private Long shopId;

    @Column("product_name")
    private String productName;

    private String description;

    private Double price;

    private Integer stock;

    @Column("approval_status")
    private ApprovalStatus approvalStatus;
}