package com.gin.msaflux.shop_service.services.imp;

import com.gin.msaflux.shop_service.repositories.ShopRepository;
import com.gin.msaflux.shop_service.services.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public Mono<String> getShopIdByOwnerId(String s) {
        return shopRepository.findByOwnerId(s).flatMap(shop -> Mono.just(shop.getId()));
    }
}
