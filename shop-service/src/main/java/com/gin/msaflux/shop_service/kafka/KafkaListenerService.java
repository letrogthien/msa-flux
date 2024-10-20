package com.gin.msaflux.shop_service.kafka;

import com.gin.msaflux.common.kafka.payload.AddProduct;
import com.gin.msaflux.shop_service.repositories.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    private final ShopRepository shopRepository ;
    private final KafkaUtils kafkaUtils ;
    @KafkaListener(
            topics = "add-product",
            concurrency = "3",
            groupId = "add-product-gr1"
    )
    public Mono<Void> listen(String checkOwnerShop) {
       return kafkaUtils.jsonNodeToObject(checkOwnerShop, AddProduct.class)
               .flatMap(addProduct ->
                   shopRepository.findByOwnerId(addProduct.getUserId())
                           .flatMap(shop -> {
                               if (shop==null){
                                   addProduct.setAccepted(false);
                                   return kafkaUtils.sendMessage("add-product-response", addProduct);
                               }
                               addProduct.setAccepted(true);
                               addProduct.setShopId(shop.getId());
                               return kafkaUtils.sendMessage("add-product-response", addProduct);
                           })
               ).then();
    }

}
