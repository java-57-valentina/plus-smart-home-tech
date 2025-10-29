package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(value = 0)
    private Integer newQuantity;
}
