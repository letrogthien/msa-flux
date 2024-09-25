package com.gin.msaflux.product_service.controllers;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.msaflux.product_service.request.UpLoadFiles;
import com.gin.msaflux.product_service.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/product/image")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;


    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> addImagesProduct(
            @RequestPart("file") FilePart file, // Sử dụng @RequestPart để nhận file từ multipart form
            @RequestPart("data") String data) throws JsonProcessingException { // Sử dụng @RequestPart để nhận object từ multipart form
        UpLoadFiles uploadFiles = new ObjectMapper().readValue(data, UpLoadFiles.class);
        return productImageService.addImagesProduct(uploadFiles, file);
    }
}
