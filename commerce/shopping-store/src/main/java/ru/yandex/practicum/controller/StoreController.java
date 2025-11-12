package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.contract.shopping.store.StoreOperations;
import ru.yandex.practicum.commerce.dto.StoreProductDto;
import ru.yandex.practicum.service.StoreService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class StoreController implements StoreOperations {

    private final StoreService service;

    @Override
    public Page<StoreProductDto> get(@RequestParam StoreProductDto.ProductCategory category,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "100") int size,
                                     @RequestParam(defaultValue = "productName,ASC") String sort) {
        log.debug("request for get list of products of category {}", category);
        return service.get(category, page, size, sort);
    }

    @Override
    public StoreProductDto get(@PathVariable UUID productId) {
        log.debug("request for get product by id: {}", productId);
        return service.get(productId);
    }

    @Override
    public Map<UUID, BigDecimal> getProductPrices(@RequestBody @NotNull Set<UUID> uuids) {
        return service.getProductPrices(uuids);
    }

    @Override
    public StoreProductDto add(@RequestBody @Valid StoreProductDto productDto) {
        log.debug("request for add product: {}", productDto);
        return service.add(productDto);
    }

    @Override
    public StoreProductDto update(@RequestBody @Valid StoreProductDto productDto) {
        log.debug("request for update product: {}", productDto);
        return service.update(productDto);
    }

    @Override
    public boolean deactivate(@RequestBody @Valid String id) {
        log.debug("request for deactivate product id: {}", id);
        return service.deactivate(id);
    }

    @Override
    public boolean updateQuantityState(@RequestParam UUID productId,
                                       @RequestParam StoreProductDto.QuantityState quantityState) {
        log.debug("request for update quantity state of product id: {}, new state: {}",
                productId,
                quantityState);
        return service.updateQuantityState(productId, quantityState);
    }
}
