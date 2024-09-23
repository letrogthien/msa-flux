package com.gin.msaflux.shop_service.kafka;

import com.gin.msaflux.shop_service.kafka.payload.AddProduct;
import com.gin.msaflux.shop_service.repositories.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    private final ShopRepository shopRepository ;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(
            topics = "add-product",
            concurrency = "3",
            groupId = "add-product-gr1"
    )
    public Mono<Void> listen(AddProduct checkOwnerShop) {
        return shopRepository.findByOwnerId(checkOwnerShop.getUserId())
                .switchIfEmpty(
                        Mono.fromRunnable(() -> {
                            checkOwnerShop.setAccepted(false);
                            kafkaTemplate.send("add-product-response", checkOwnerShop);
                        })
                )
                .flatMap(shop -> {
                    checkOwnerShop.setShopId(shop.getId());
                    return Mono.fromRunnable(() -> {
                        checkOwnerShop.setAccepted(true);
                        kafkaTemplate.send("add-product-response", checkOwnerShop);
                    }).then();
                });
    }
}
