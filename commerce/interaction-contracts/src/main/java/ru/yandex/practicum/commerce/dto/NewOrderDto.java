package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NewOrderDto {

    @NotNull
    @JsonProperty("shoppingCartId")
    private UUID cartId;

    @NotNull
    private DeliveryAddress deliveryAddress;
}
