package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseGoodQuantityDto {
    @NotNull
    private UUID productId;

    @Min(value = 0)
    private Integer quantity;
}
