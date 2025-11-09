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
    // private UUID paymentId;
    private double volume; // объём товаров;
    private double weight; // вес товаров;
    private boolean isFragile; // признак хрупкости;
    private double productsPrice; // цена всех товаров;
    // private double deliveryPrice; // цена доставки.
    // private double totalPrice; // итоговая цена;

    private Map<UUID, Integer> products;
    private OrderState state;

    private Timestamp createdAt;
}
