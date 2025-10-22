package ru.yandex.practicum.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductDto {

    @JsonProperty("productId")
    private String id;

    @JsonProperty("productName")
    private String name;

    private String description;

    private String imageSrc;

    @JsonProperty("quantityState")
    private QuantityState quantityState;

    @JsonProperty("productState")
    private ProductState state;

    @JsonProperty("productCategory")
    private ProductCategory category;

    private Long price;


    public enum QuantityState {
        ENDED, AVAILABLE, LOW_STOCK
    }

    public enum ProductState {
        ACTIVE, INACTIVE, ARCHIVED
    }

    public enum ProductCategory {
        DEVICE, ACCESSORY, SERVICE
    }
}
