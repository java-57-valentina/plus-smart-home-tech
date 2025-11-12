package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.stream.Collectors;

@UtilityClass
public class CartMapper {
    public static ShoppingCartDto toDto(ShoppingCart cart) {
        return ShoppingCartDto.builder()
                .id(cart.getId())
                .state(cart.getState())
                .username(cart.getUsername())
                .products(cart.getProducts().stream()
                        .collect(Collectors.toMap(
                        CartProduct::getProductId,
                        CartProduct::getQuantity
                )))
                .build();
    }
}
