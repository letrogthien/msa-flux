package com.gin.msaflux.product_service.services;

import com.gin.msaflux.product_service.request.UpLoadFiles;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductImageService {
    Mono<Void> addImagesProduct(UpLoadFiles upLoadFiles, FilePart filePart) ;
    Mono<Void> deleteImagesProduct(Long productId, Long productImageId) ;
}
