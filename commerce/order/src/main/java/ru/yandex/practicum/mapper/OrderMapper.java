package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.OrderState;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
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
                .isFragile(order.isFragile())
                .weight(order.getWeight())
                .volume(order.getVolume())
                .productsPrice(order.getProductsPrice())
                .products(order.getProducts().stream()
                        .collect(Collectors.toMap(
                                        OrderProductInfo::getProductId,
                                        OrderProductInfo::getQuantity)))
                .createdAt(order.getCreatedAt())
                .build();
    }

    public Order fromDto(ShoppingCartDto shoppingCartDto, BookedProductsDto bookedProductsDto) {
        return Order.builder()
                .cartId(shoppingCartDto.getId())
                .username(shoppingCartDto.getUsername())
                .state(OrderState.NEW)
                .username(shoppingCartDto.getUsername())
                .fragile(bookedProductsDto.getFragile())
                .volume(bookedProductsDto.getDeliveryVolume())
                .weight(bookedProductsDto.getDeliveryWeight())
                .build();
    }
}
