package ru.yandex.practicum.commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReturnOrderDto {
    private UUID orderId;
    private Map<String, Integer> products;
}
