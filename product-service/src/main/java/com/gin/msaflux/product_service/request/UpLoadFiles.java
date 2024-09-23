package com.gin.msaflux.product_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpLoadFiles {
    private String productId;
    private String altText;
    private boolean isPrimary;
}
