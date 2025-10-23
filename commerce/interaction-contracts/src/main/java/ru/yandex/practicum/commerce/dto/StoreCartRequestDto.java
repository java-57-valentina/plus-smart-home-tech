package ru.yandex.practicum.commerce.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreCartRequestDto {

    private UUID shoppingCartId;
    private Map<UUID, Integer> products;
}
