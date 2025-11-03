package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
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

    @Transactional
    public DeliveryDto delivery(DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.fromDto(deliveryDto);
        Delivery saved = repository.save(delivery);
        return DeliveryMapper.toDto(saved);
    }

    public void deliverySuccessful(UUID orderId) {
        
    }

    public void deliveryPicked(UUID orderId) {

    }

    public void deliveryFailed(UUID orderId) {

    }

    public void deliveryCost(OrderDto orderDto) {

    }
}
