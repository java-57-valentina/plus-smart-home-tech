package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    @JsonProperty("shoppingCartId")
    private UUID id;

    private Map<UUID, Integer> products;
}
