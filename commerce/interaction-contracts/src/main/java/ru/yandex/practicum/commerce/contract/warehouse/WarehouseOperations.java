package ru.yandex.practicum.commerce.contract.warehouse;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.WarehouseAddressDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodQuantityDto;

public interface WarehouseOperations {

    // Добавить новый товар на склад.
    @PutMapping
    void add(@RequestBody @Valid WarehouseGoodDto goodDto);

    // Принять товар на склад
    @PostMapping("/add")
    void add(@RequestBody @Valid WarehouseGoodQuantityDto request);

    // Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
    @PostMapping
    boolean check(@RequestBody @Valid ShoppingCartDto productDto);

    // Предоставить адрес склада для расчёта доставки.
    @GetMapping("/address")
    WarehouseAddressDto getAddress();
}
