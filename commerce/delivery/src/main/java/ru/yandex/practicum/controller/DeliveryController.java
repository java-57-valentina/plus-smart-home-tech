package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.delivery.order.DeliveryOperations;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryOperations {

    private final DeliveryService service;

    @Override
    public DeliveryDto delivery(DeliveryDto deliveryDto) {
        log.debug("request for plan delivery: {}", deliveryDto);
        return service.delivery(deliveryDto);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        log.debug("request for handle successful delivery (orderId: {})", orderId);
        service.deliverySuccessful(orderId);
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        log.debug("request for handle delivery picked: (orderId: {})", orderId);
        service.deliveryPicked(orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        log.debug("request for handle delivery failed: (orderId: {})", orderId);
        service.deliveryFailed(orderId);
    }

    @Override
    public double deliveryCost(OrderDto orderDto) {
        log.debug("request for calculate delivery cost: {}", orderDto);
        service.deliveryCost(orderDto);
        return 0;
    }
}
