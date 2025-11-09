package ru.yandex.practicum.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseGoodDtoIn {

    @NotNull
    private UUID productId;

    private boolean fragile;

    @Valid
    @NotNull(message = "Dimension is required")
    private Dimension dimension;

    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "100000.0", message = "Weight cannot exceed 1000 kg")
    private double weight;
}