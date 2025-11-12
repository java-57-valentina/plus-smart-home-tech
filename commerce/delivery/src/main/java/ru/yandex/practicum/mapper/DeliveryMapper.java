package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@UtilityClass
public class DeliveryMapper {

    public DeliveryDto toDto(Delivery delivery) {

        return DeliveryDto.builder()
                .deliveryId(delivery.getId())
                .state(delivery.getState())
                .orderId(delivery.getOrderId())
                .isFragile(delivery.isFragile())
                .weight(delivery.getWeight())
                .volume(delivery.getVolume())
                .fromAddress(AddressMapper.toDto(delivery.getFromAddress()))
                .toAddress(AddressMapper.toDto(delivery.getToAddress()))
                .build();
    }

    public static Delivery fromDto(DeliveryDto deliveryDto) {

        return Delivery.builder()
                .orderId(deliveryDto.getOrderId())
                .isFragile(deliveryDto.isFragile())
                .weight(deliveryDto.getWeight())
                .volume(deliveryDto.getVolume())
                .fromAddress(AddressMapper.fromDto(deliveryDto.getFromAddress()))
                .toAddress(AddressMapper.fromDto(deliveryDto.getToAddress()))
                .build();
    }
}
