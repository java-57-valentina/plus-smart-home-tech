package ru.yandex.practicum.commerce.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.dto.StoreProductDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    @JsonProperty("shoppingCartId")
    private String id;

    private List<StoreProductDto> products;
}
