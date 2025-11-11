package ru.yandex.practicum.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID id;
    private UUID cartId;
    private String username;

    private UUID deliveryId;
    private UUID paymentId;

    private Double volume; // объём товаров;
    private Double weight; // вес товаров;
    private Boolean fragile; // признак хрупкости;

    private Double productsPrice; // цена всех товаров;
    private Double deliveryPrice; // цена доставки.
    private Double totalPrice; // итоговая цена;

    private Map<UUID, Integer> products;
    private OrderState state;

    private Timestamp createdAt;
}
