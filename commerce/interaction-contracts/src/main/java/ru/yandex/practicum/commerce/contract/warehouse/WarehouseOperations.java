package ru.yandex.practicum.commerce.contract.warehouse;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseOperations {

    @GetMapping("/{productId}")
    WarehouseGoodDtoOut get(@PathVariable @NotNull UUID productId);

    @GetMapping
    Collection<WarehouseGoodDtoOut> getAll();

    // Добавить новый товар на склад.
    @PutMapping
    void add(@RequestBody @Valid WarehouseGoodDtoIn goodDto);

    // Принять товар на склад
    @PostMapping("/add")
    void add(@RequestBody @Valid WarehouseGoodQuantityDto request);

    // Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
    @PostMapping
    BookedProductsDto check(@RequestBody @Valid ShoppingCartDto productDto) throws FeignException;

    @PostMapping("/assembly")
    void assemblyProducts(@RequestParam @NotNull UUID orderId,
                          @RequestBody Map<UUID, Integer> products);

    // Предоставить адрес склада для расчёта доставки
    @GetMapping("/address")

    AddressDto getAddress();

    @PostMapping("/return")
    void returnItems(@RequestParam @NotNull UUID orderId,
                     @RequestBody Map<UUID, Integer> products);
}
