package com.gin.msaflux.product_service.services.impl;

import com.gin.msaflux.product_service.models.ProductImage;
import com.gin.msaflux.product_service.repositories.ProductImageRepository;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.request.UpLoadFiles;
import com.gin.msaflux.product_service.services.ProductImageService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FilenameUtils;
import org.reactivestreams.Publisher;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Flushable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    private static final String ROOT_LOCATION = "/tmp/";


    @Override
    public Mono<Void> addImagesProduct(UpLoadFiles upLoadFiles, FilePart filePart) {
        return  ReactiveSecurityContextHolder.getContext().flatMap(securityContext ->
             productRepository.findById(upLoadFiles.getProductId())
                    .flatMap(product -> {
                        if(!securityContext.getAuthentication().getName().equalsIgnoreCase(product.getSellerId())){
                            return Mono.error(new BadCredentialsException("You are not allowed to add product images"));
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                        String timestamp = LocalDateTime.now().format(formatter);
                        String fileName = filePart.filename();
                        String formattedFileName = product.getId() + "_" + timestamp + "_" + fileName;
                        Path path = Paths.get(ROOT_LOCATION + formattedFileName);
                        return filePart.transferTo(path)
                                .then(productImageRepository.save(ProductImage.builder()
                                        .imageUrl(path.toString())
                                        .productId(product.getId())
                                        .altText(upLoadFiles.getAltText())
                                        .isPrimary(upLoadFiles.isPrimary())
                                        .build()))
                                .then();

                    }).then()

        );
    }

    @Override
    public Mono<Void> deleteImagesProduct(Long productId, Long productImageId) {
        return null;
    }

    private boolean isValidExtension(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1).toLowerCase();
        }

        return extension.equals("jpg") || extension.equals("png");
    }


}
