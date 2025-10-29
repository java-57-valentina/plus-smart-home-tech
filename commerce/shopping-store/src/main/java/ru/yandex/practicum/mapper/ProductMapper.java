package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.StoreProductDto;
import ru.yandex.practicum.model.Product;

@UtilityClass
public class ProductMapper {
    public Product fromDto(StoreProductDto productDto) {
        return Product.builder()
                .productName(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .state(productDto.getState())
                .quantityState(productDto.getQuantityState())
                .category(productDto.getCategory())
                .imageSrc(productDto.getImageSrc())
                .build();
    }

    public StoreProductDto toDto(Product product) {
        return StoreProductDto.builder()
                .id(product.getId().toString())
                .name(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .state(product.getState())
                .quantityState(product.getQuantityState())
                .category(product.getCategory())
                .imageSrc(product.getImageSrc())
                .build();
    }
}
