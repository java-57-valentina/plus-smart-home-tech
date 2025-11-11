package ru.yandex.practicum.commerce.contract.delivery;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryOperations {

    @GetMapping("/{id}")
    DeliveryDto getById(@PathVariable UUID id);

    @PutMapping
    DeliveryDto delivery(@RequestBody DeliveryDto deliveryDto);

    // Эмуляция получения товара в доставку
    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID deliveryId);

    // Эмуляция успешной доставки товара
    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID deliveryId);

    // Эмуляция неудачного вручения товара
    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID deliveryId);

    // Расчёт полной стоимости доставки заказа
    @PostMapping("/cost")
    double calculateCost(@RequestBody OrderDto orderDto);
}