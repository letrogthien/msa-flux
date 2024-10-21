package com.gin.msaflux.product_service.kafka;



import com.gin.msaflux.common.kafka.payload.AddProduct;
import com.gin.msaflux.common.kafka.payload.OrderPayload;
import com.gin.msaflux.product_service.common.ApprovalStatus;
import com.gin.msaflux.product_service.repositories.ProductRepository;
import com.gin.msaflux.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class KafkaListenerService{
    private final ProductRepository productRepository;
    private final ProductService productService;
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




    @KafkaListener(
            topics = "inventory-check-success",
            concurrency = "3",
            groupId = "inventory-check-success"
    )
    public Mono<Void> caculateAmount(String orderPayload){
        return kafkaUtils.jsonNodeToObject(orderPayload, OrderPayload.class)
                .flatMap(
                        orderP ->
                             productService.caculateAmount(Flux.fromIterable(orderP.getOrderItems()).map(OrderPayload.OrderItemPayload::getProductId)).flatMap(
                                    total -> {
                                        orderP.setTotalPrice(total);
                                        return kafkaUtils.sendMessage("caculate-amount", orderP);
                                    }
                            )
                ).then();

    }
}

