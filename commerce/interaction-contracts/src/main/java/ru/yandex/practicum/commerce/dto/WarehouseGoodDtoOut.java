package ru.yandex.practicum.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseGoodDtoOut {
    private UUID productId;
    private int quantity;
    private boolean fragile;
    private Dimension dimension;
    private double weight;
}