package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.DeliveryState;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository repository;
    private final OrderOperations orderClient;

    public DeliveryDto getById(UUID id) {
        return DeliveryMapper.toDto(getDelivery(id));
    }

    @Transactional
    public DeliveryDto delivery(DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.fromDto(deliveryDto);
        delivery.setState(DeliveryState.CREATED);
        Delivery saved = repository.save(delivery);
        return DeliveryMapper.toDto(saved);
    }

    @Transactional
    public void deliverySuccessful(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setState(DeliveryState.DELIVERED);
        orderClient.delivery(delivery.getOrderId());
    }

    @Transactional
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        log.debug("delivery.getOrderId : {}", delivery.getOrderId());
        orderClient.deliveryStarted(delivery.getOrderId());
    }

    @Transactional
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setState(DeliveryState.FAILED);
        orderClient.deliveryFailed(delivery.getOrderId());
    }

    public void deliveryCost(OrderDto orderDto) {

    }

    private Delivery getDelivery(UUID deliveryId) {
        return repository
                .findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery", deliveryId));
    }
}
