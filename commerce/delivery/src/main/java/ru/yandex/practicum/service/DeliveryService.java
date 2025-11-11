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
import ru.yandex.practicum.model.Address;
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

    private static final Double BASE_COST = 5.0;
    private static final Double WAREHOUSE_ADDRESS_2_SURCHARGE = 2.0;
    private static final Double FRAGILE_SURCHARGE = 0.2;
    private static final Double WEIGHT_SURCHARGE = 0.3;
    private static final Double VOLUME_SURCHARGE = 0.2;
    private static final Double ADDRESS_DELIVERY_SURCHARGE = 0.2;

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

    public double deliveryCost(OrderDto orderDto) {
//        if (!validOrder(orderDto)) {
//            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно данных для расчета стоимости доставки");
//        }
        Delivery delivery = getDelivery(orderDto.getDeliveryId());
        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();
        double cost = BASE_COST;

        switch (fromAddress.getCity()) {
            case "ADDRESS_1":
                cost += cost;
                break;
            case "ADDRESS_2":
                cost += cost * WAREHOUSE_ADDRESS_2_SURCHARGE;
                break;
            default:
                break;
        }
        if (orderDto.getFragile()) {
            cost += cost * FRAGILE_SURCHARGE;
        }
        cost += orderDto.getWeight() * WEIGHT_SURCHARGE;
        cost += orderDto.getVolume() * VOLUME_SURCHARGE;
        if (!(fromAddress.getCountry().equals(toAddress.getCountry())
                && fromAddress.getCity().equals(toAddress.getCity())
                && fromAddress.getStreet().equals(toAddress.getStreet()))) {
            cost += cost * ADDRESS_DELIVERY_SURCHARGE;
        }
        log.info("Delivery cost: {}", cost);
        return cost;
    }

    private Delivery getDelivery(UUID deliveryId) {
        return repository
                .findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery", deliveryId));
    }
}
