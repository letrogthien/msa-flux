package com.gin.msaflux.shop_service.services;


import com.gin.msaflux.shop_service.models.Shop;
import reactor.core.publisher.Mono;

public interface ShopService {
    Mono<String> getShopIdByOwnerId(String s);
    Mono<Shop> addShop(Shop s);
}
