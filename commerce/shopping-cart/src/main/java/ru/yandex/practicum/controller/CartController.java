package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.cart.CartOperations;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.UpdateQuantityRequest;
import ru.yandex.practicum.service.CartService;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class CartController implements CartOperations {

    private final CartService cartService;

    @Override
    public ShoppingCartDto get(@RequestParam String username) {
        log.debug("request for get shopping cart by username: {}", username);
        return cartService.get(username);
    }

    @Override
    public ShoppingCartDto getById(UUID cartId) {
        log.debug("request for get shopping cart by id: {}", cartId);
        return cartService.get(cartId);
    }

    // Добавить товар в корзину.
    @Override
    public ShoppingCartDto add(@RequestParam String username, @RequestBody Map<UUID, Integer> products) {
        log.debug("request for add products to shopping cart: {}", products);
        return cartService.add(username, products);
    }


    // Деактивация корзины товаров для пользователя.
    @Override
    public void deactivate(@RequestParam String username) {
        log.debug("request for delete shopping cart: {}", username);
        cartService.deactivate(username);
    }

    // Удалить указанные товары из корзины пользователя.
    @Override
    public ShoppingCartDto remove(@RequestParam String username,
                                  @RequestBody Collection<UUID> ids) {
        log.debug("request for remove products form shopping cart of user: {}", username);
        return cartService.remove(username, ids);
    }

    @Override
    public ShoppingCartDto changeQuantity(@RequestParam @NotBlank String username,
                                          @RequestBody @Valid UpdateQuantityRequest updateQuantityRequest) {
        log.debug("request for changeQuantity of products {} in {}'s cart: ", updateQuantityRequest, username);
        return cartService.changeQuantity(username, updateQuantityRequest);
    }

    @Override
    public BookedProductsDto bookingProducts(@RequestParam @NotBlank String username) {
        log.debug("request for booking products in shopping cart of user: {}", username);
        return cartService.bookingProductsFromShoppingCart(username);
    }

}
