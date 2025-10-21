package ru.yandex.practicum.commerce.contract.shopping.cart;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartClient extends CartOperations {
}
