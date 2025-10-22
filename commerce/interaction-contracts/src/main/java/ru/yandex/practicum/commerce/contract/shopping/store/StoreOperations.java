package ru.yandex.practicum.commerce.contract.shopping.store;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.StoreProductDto;

import org.springframework.data.domain.Page;
import java.util.UUID;

public interface StoreOperations {

    @GetMapping
    Page<StoreProductDto> get(@RequestParam StoreProductDto.ProductCategory category,
                              @RequestParam int page,
                              @RequestParam int size,
                              @RequestParam String sort);

    @GetMapping("/{productId}")
    StoreProductDto get(@PathVariable String productId);

    @PutMapping
    StoreProductDto add(@RequestBody StoreProductDto productDto);

    @PostMapping
    StoreProductDto update(@RequestBody StoreProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean deactivate(@RequestBody String id);
}
