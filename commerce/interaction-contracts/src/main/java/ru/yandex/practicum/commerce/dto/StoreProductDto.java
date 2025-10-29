package ru.yandex.practicum.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @JsonProperty("productName")
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Size(min = 3, max = 1000)
    private String description;

    @NotBlank
    private String imageSrc;

    @NotNull
    @JsonProperty("quantityState")
    private QuantityState quantityState;

    @NotNull
    @JsonProperty("productState")
    private ProductState state;

    @NotNull
    @JsonProperty("productCategory")
    private ProductCategory category;

    @NotNull
    @Positive
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
