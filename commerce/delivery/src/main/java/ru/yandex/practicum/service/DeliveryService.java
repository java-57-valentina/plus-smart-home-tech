package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.DeliveryState;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.exception.ConflictException;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository repository;
    private final OrderOperations orderClient;

    private static final Double BASE_COST = 5.0;
    private static final Double WAREHOUSE_ADDRESS_1_MULTIPLIER = 2.0;
    private static final Double WAREHOUSE_ADDRESS_2_MULTIPLIER = 3.0;
    private static final Double ADDRESS_DELIVERY_MULTIPLIER = 1.2;

    private static final Double FRAGILE_SURCHARGE = 1.2;
    private static final Double WEIGHT_SURCHARGE = 0.3;
    private static final Double VOLUME_SURCHARGE = 0.2;

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
        try {
            orderClient.delivery(delivery.getOrderId());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new ConflictException(e.getMessage());
            }
            throw e;
        }
    }

    @Transactional
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        try {
            orderClient.deliveryStarted(delivery.getOrderId());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new ConflictException(e.getMessage());
            }
            throw e;
        }
    }

    @Transactional
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setState(DeliveryState.FAILED);
        try {
            orderClient.deliveryFailed(delivery.getOrderId());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new ConflictException(e.getMessage());
            }
            throw e;
        }
    }

    public double deliveryCost(OrderDto orderDto) {
        Delivery delivery = getDelivery(orderDto.getDeliveryId());

        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();

        double totalCost = BASE_COST;

        totalCost *= fromAddress.getCity().equals("ADDRESS_1")
                ? WAREHOUSE_ADDRESS_1_MULTIPLIER
                : WAREHOUSE_ADDRESS_2_MULTIPLIER;

        if (orderDto.getFragile())
            totalCost *= FRAGILE_SURCHARGE;

        totalCost += delivery.getWeight() * WEIGHT_SURCHARGE;
        totalCost += delivery.getVolume() * VOLUME_SURCHARGE;

        if (!isSameStreet(fromAddress, toAddress))
            totalCost *= ADDRESS_DELIVERY_MULTIPLIER;

        return totalCost;
    }

    private boolean isSameStreet(Address address1, Address address2) {
        if (!address1.getCountry().equals(address2.getCountry()))
            return false;
        if (!address1.getCity().equals(address2.getCity()))
            return false;
        return address1.getStreet().equals(address2.getStreet());
    }

    private Delivery getDelivery(UUID deliveryId) {
        return repository
                .findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery", deliveryId));
    }
}
