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
    public DeliveryDto getById(UUID id) {
        return service.getById(id);
    }

    @Override
    public DeliveryDto delivery(DeliveryDto deliveryDto) {
        log.debug("request for plan delivery: {}", deliveryDto);
        return service.delivery(deliveryDto);
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        log.debug("request for handle successful delivery (orderId: {})", deliveryId);
        service.deliverySuccessful(deliveryId);
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        log.debug("request for handle delivery picked: (orderId: {})", deliveryId);
        service.deliveryPicked(deliveryId);
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        log.debug("request for handle delivery failed: {}", deliveryId);
        service.deliveryFailed(deliveryId);
    }

    @Override
    public double deliveryCost(OrderDto deliveryId) {
        log.debug("request for calculate delivery cost: {}", deliveryId);
        service.deliveryCost(deliveryId);
        return 0;
    }
}
