package ru.yandex.practicum.commerce.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID orderId;
    private UUID shoppingCartId;
    private Map<String, Integer> products;
    private UUID paymentId;
    private UUID deliveryId;
    private String state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
}
