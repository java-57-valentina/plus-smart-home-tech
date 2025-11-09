package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService service;

    @Override
    public WarehouseGoodDtoOut get(UUID productId) {
        return service.get(productId);
    }

    @Override
    public Collection<WarehouseGoodDtoOut> getAll() {
        return service.getAll();
    }

    @Override
    public void add(@RequestBody @Valid WarehouseGoodDtoIn goodDto) {
        log.debug("request for add new good: {}", goodDto);
        service.add(goodDto);
    }

    @Override
    public void add(@RequestBody @Valid WarehouseGoodQuantityDto request) {
        log.debug("request for update quantity of good id:{}", request);
        service.updateQuantity(request);
    }

    @Override
    public BookedProductsDto check(@RequestBody @Valid ShoppingCartDto cardRequestDto) {
        log.debug("request for check cart: {}", cardRequestDto);
        return service.check(cardRequestDto.getProducts());
    }

    @Override
    public void assemblyProducts(UUID orderId, Map<UUID, Integer> products) {
        log.debug("request for assembly products {} for order {}", products, orderId);
        service.assemblyProducts(orderId, products);
    }

    @Override
    public AddressDto getAddress() {
        log.debug("request for get address");
        return service.getAddress();
    }

    @Override
    public void returnItems(UUID orderId, Map<UUID, Integer> products) {
        log.debug("TODO: return items {} from order id:{}", products, orderId);
        service.returnItems(orderId, products);
    }
}
