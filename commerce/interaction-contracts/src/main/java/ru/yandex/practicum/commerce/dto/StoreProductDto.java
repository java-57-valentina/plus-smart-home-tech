package ru.yandex.practicum.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
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

    private Double price;


    public enum QuantityState {
        ENDED, FEW, ENOUGH, MANY
    }

    public enum ProductState {
        ACTIVE, DEACTIVATE
    }

    public enum ProductCategory {
        CONTROL, SENSORS, LIGHTING
    }
}
