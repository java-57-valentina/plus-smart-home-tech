package ru.yandex.practicum.commerce.contract.delivery.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryOperations {

    @PutMapping
    DeliveryDto delivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/cost")
    double deliveryCost(@RequestBody OrderDto orderDto);
}