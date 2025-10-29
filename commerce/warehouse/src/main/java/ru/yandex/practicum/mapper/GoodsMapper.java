package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDto;
import ru.yandex.practicum.model.WarehouseGood;

@UtilityClass
public class GoodsMapper {
    public static WarehouseGood fromDto(WarehouseGoodDto goodDto) {
        return WarehouseGood.builder()
                .productId(goodDto.getProductId())
                .fragile(goodDto.isFragile())
                .weight(goodDto.getWeight())
                .width(goodDto.getDimension().getWidth())
                .height(goodDto.getDimension().getHeight())
                .depth(goodDto.getDimension().getDepth())
                .build();
    }
}
