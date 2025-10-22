package ru.yandex.practicum.commerce.contract.shopping.cart;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;

import java.util.List;

public interface CartOperations {

    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @PutMapping
    ShoppingCartDto put(@RequestParam String username,
                        @RequestBody ShoppingCartDto cartDto);

    @DeleteMapping
    void delete();

    @PostMapping("/remove")
    ShoppingCartDto remove(@RequestParam String username,
                           @RequestBody List<String> ids);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username);
}
