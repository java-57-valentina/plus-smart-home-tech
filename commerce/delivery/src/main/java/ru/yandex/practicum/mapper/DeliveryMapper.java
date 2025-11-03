package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.DeliveryDto;
import ru.yandex.practicum.commerce.dto.DeliveryState;
import ru.yandex.practicum.model.Delivery;

@UtilityClass
public class DeliveryMapper {

    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getId())
                .state(delivery.getState())
                .orderId(delivery.getOrderId())
                // .fromAddress(delivery.)
                //.toAddress(delivery.)
                .build();
    }

    public static Delivery fromDto(DeliveryDto deliveryDto) {
        Delivery delivery = new Delivery();
        delivery.setState(DeliveryState.CREATED);
        delivery.setOrderId(deliveryDto.getOrderId());
        return delivery;
    }
}
