package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.Dimension;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDtoIn;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDtoOut;
import ru.yandex.practicum.model.WarehouseGood;

@UtilityClass
public class GoodsMapper {
    public static WarehouseGood fromDto(WarehouseGoodDtoIn goodDto) {
        return WarehouseGood.builder()
                .productId(goodDto.getProductId())
                .fragile(goodDto.isFragile())
                .weight(goodDto.getWeight())
                .width(goodDto.getDimension().getWidth())
                .height(goodDto.getDimension().getHeight())
                .depth(goodDto.getDimension().getDepth())
                .build();
    }

    public static WarehouseGoodDtoOut toDto(WarehouseGood good) {
        return WarehouseGoodDtoOut.builder()
                .productId(good.getProductId())
                .fragile(good.isFragile())
                .weight(good.getWeight())
                .quantity(good.getQuantity())
                .dimension(new Dimension(good.getWidth(), good.getHeight(), good.getDepth()))
                .build();
    }
}
