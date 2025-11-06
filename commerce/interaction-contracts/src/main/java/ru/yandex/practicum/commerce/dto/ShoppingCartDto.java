package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    @NotNull
    @JsonProperty("shoppingCartId")
    private UUID id;

    private String username;

    @NotNull
    @ToString.Exclude
    private Map<UUID, Integer> products;

    ShoppingCartState state;
}
