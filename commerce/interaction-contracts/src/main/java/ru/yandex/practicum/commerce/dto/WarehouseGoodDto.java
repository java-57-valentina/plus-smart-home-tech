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
public class WarehouseGoodDto {

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimension {

        @Positive(message = "Width must be positive")
        @DecimalMax(value = "1000.0", message = "Width cannot exceed 10 meters")
        @DecimalMin(value = "1.0")
        private double width;

        @Positive(message = "Height must be positive")
        @DecimalMin(value = "1.0")
        @DecimalMax(value = "1000.0", message = "Height cannot exceed 10 meters")
        private double height;

        @Positive(message = "Depth must be positive")
        @DecimalMin(value = "1.0")
        @DecimalMax(value = "1000.0", message = "Depth cannot exceed 10 meters")
        private double depth;
    }
}