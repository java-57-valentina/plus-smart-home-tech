package ru.yandex.practicum.commerce.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {
    private UUID productId;
    private Integer newQuantity;
}
