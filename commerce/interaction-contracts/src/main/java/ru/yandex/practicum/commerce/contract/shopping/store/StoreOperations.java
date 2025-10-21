package ru.yandex.practicum.commerce.contract.shopping.store;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface StoreOperations {

    @GetMapping
    String get();

    @GetMapping("/{productId}")
    String get(Long id);

    @PutMapping
    String put();

    @PostMapping
    String post();

    @PostMapping("/removeProductFromStore")
    String remove();

    @PostMapping("/quantityState")
    String state();
}
