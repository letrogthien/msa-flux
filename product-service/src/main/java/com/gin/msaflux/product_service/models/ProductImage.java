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
@Table("product_images")
public class ProductImage {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("image_url")
    private String imageUrl;

    @Column("is_primary")
    private Boolean isPrimary;
}
