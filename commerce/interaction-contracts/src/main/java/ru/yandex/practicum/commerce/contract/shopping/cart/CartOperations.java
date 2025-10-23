package ru.yandex.practicum.commerce.contract.shopping.cart;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.UpdateQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartOperations {

    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @PutMapping
    ShoppingCartDto add(@RequestParam String username,
                        @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void delete(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto remove(@RequestParam String username,
                           @RequestBody List<UUID> ids);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username,
                                   @RequestBody UpdateQuantityRequest updateQuantityRequest);
}
