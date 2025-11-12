package ru.yandex.practicum.commerce.contract.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.UpdateQuantityRequest;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartOperations {

    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @GetMapping("/{cartId}")
    ShoppingCartDto getById(@PathVariable UUID cartId);

    @PutMapping
    ShoppingCartDto add(@RequestParam String username,
                        @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivate(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto remove(@RequestParam String username,
                           @RequestBody Collection<UUID> ids);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam @NotBlank String username,
                                   @RequestBody @Valid UpdateQuantityRequest updateQuantityRequest);

    @PostMapping("/book")
    BookedProductsDto bookingProducts(@RequestParam @NotBlank String username);
}
