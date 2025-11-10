package ru.yandex.practicum.commerce.contract.shopping.store;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.StoreProductDto;

import org.springframework.data.domain.Page;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreOperations {

    @GetMapping
    Page<StoreProductDto> get(@RequestParam StoreProductDto.ProductCategory category,
                              @RequestParam int page,
                              @RequestParam int size,
                              @RequestParam String sort);

    @GetMapping("/{productId}")
    StoreProductDto get(@PathVariable UUID productId);

    @PutMapping
    StoreProductDto add(@RequestBody @Valid StoreProductDto productDto);

    @PostMapping
    StoreProductDto update(@RequestBody @Valid StoreProductDto productDto);

    @PostMapping("/quantityState")
    boolean updateQuantityState(@RequestParam UUID productId,
                                @RequestParam StoreProductDto.QuantityState quantityState);

    @PostMapping("/removeProductFromStore")
    boolean deactivate(@RequestBody @Valid String id);
}
