package com.gin.msaflux.product_service.kafka;



import com.gin.msaflux.product_service.common.ApprovalStatus;
import com.gin.msaflux.product_service.kafka.payload.AddProduct;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class KafkaListenerService{
    private final ProductRepository productRepository;
    private final KafkaUtils kafkaUtils;
    @KafkaListener(
            topics = "add-product-response",
            concurrency = "3",
            groupId = "add-product-response"
    )
    public Mono<Void> addProductResponse(String checkOwnerShop) {
        return kafkaUtils.jsonNodeToObject(checkOwnerShop, AddProduct.class)
                .flatMap(addProduct ->
                        productRepository.findById(addProduct.getProductId())
                        .flatMap(product -> {
                            if (!addProduct.isAccepted()){
                                product.setApprovalStatus(ApprovalStatus.REJECTED);
                                return productRepository.save(product).then();
                            }
                            product.setShopId(addProduct.getShopId());
                            product.setApprovalStatus(ApprovalStatus.APPROVED);
                            return productRepository.save(product).then();
                        }));
    }
}

