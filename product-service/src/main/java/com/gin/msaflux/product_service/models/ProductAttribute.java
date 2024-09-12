package com.gin.msaflux.product_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_attributes")
public class ProductAttribute {
    @Id
    private String id;
    private String productId;
    private String name;
    private String value;

}
