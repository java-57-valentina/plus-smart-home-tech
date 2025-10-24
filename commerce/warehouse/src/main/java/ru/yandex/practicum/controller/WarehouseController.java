package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.WarehouseAddressDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodQuantityDto;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService warehouseService;

    @Override
    public void add(@RequestBody @Valid WarehouseGoodDto goodDto) {
        log.debug("request for add new good: {}", goodDto);
        warehouseService.add(goodDto);
    }

    @Override
    public void add(@RequestBody @Valid WarehouseGoodQuantityDto request) {
        log.debug("request for update quantity of good id:{}", request);
        warehouseService.updateQuantity(request);
    }

    @Override
    public boolean check(@RequestBody @Valid ShoppingCartDto cardRequestDto) {
        return warehouseService.check(cardRequestDto);
    }

    @Override
    public WarehouseAddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
