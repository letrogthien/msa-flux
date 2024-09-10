package com.gin.msaflux.product_service.request;
import com.gin.msaflux.product_service.models.Category;
import com.gin.msaflux.product_service.models.Product;
import lombok.*;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRq {
    private Product product;
    private List<String> imagesUrl;
    private Category category;
}
