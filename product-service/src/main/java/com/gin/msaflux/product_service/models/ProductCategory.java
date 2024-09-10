package com.gin.msaflux.product_service.models;


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
@Table("product_categories")
public class ProductCategory {

    @Id
    @Column("product_id")
    private Long productId;

    @Id
    @Column("category_id")
    private Long categoryId;
}