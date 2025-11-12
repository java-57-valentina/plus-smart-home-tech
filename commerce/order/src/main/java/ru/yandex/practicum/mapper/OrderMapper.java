package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderProductInfo;

import java.util.stream.Collectors;

@UtilityClass
public class OrderMapper {
    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .state(order.getState())
                .username(order.getUsername())
                .cartId(order.getCartId())
                .fragile(order.getFragile())
                .weight(order.getWeight())
                .volume(order.getVolume())
                .deliveryId(order.getDeliveryId())
                .paymentId(order.getPaymentId())
                .productsPrice(order.getProductsPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .totalPrice(order.getTotalPrice())
                .products(order.getProducts().stream()
                        .collect(Collectors.toMap(
                                        OrderProductInfo::getProductId,
                                        OrderProductInfo::getQuantity)))
                .createdAt(order.getCreatedAt())
                .build();
    }

    public Order fromDto(NewOrderDto orderDto, BookedProductsDto bookedProductsDto) {
        return Order.builder()
                .cartId(orderDto.getCartId())
                .state(OrderState.NEW)
                .fragile(bookedProductsDto.getFragile())
                .volume(bookedProductsDto.getDeliveryVolume())
                .weight(bookedProductsDto.getDeliveryWeight())
                .build();
    }
}
