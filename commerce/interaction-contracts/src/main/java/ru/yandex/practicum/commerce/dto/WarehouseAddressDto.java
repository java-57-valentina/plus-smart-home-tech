package ru.yandex.practicum.commerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WarehouseAddressDto {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
