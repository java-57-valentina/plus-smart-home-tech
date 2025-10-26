package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.UpdateQuantityRequest;
import ru.yandex.practicum.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.exception.NotEnoughProductsException;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.CardRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CardRepository repository;
    private final WarehouseClient warehouseClient;

    public ShoppingCartDto get(String username) {
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Shopping cart of user", username));

        return CartMapper.toDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto add(String username, Map<UUID, Integer> products) {

        ShoppingCart cart = getOrCreateCart(username);

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            Integer quantity = entry.getValue();
            if (quantity == 0)
                continue;

            CartProduct product = getOrCreateCartProduct(cart, entry.getKey());
            product.setQuantity(product.getQuantity() + quantity);
        }

        return CartMapper.toDto(cart);
    }

    @Transactional
    public void delete(String username) {
        if (!repository.existsByUsername(username)) {
            return;
        }
        repository.deleteByUsername(username);
    }

    @Transactional
    public ShoppingCartDto remove(String username, List<UUID> ids) {
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Shopping cart of user", username));

        shoppingCart.getProducts().removeIf(product
                -> ids.contains(product.getProductId()));
        return CartMapper.toDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto changeQuantity(String username, UpdateQuantityRequest updateQuantityDto) {

        ShoppingCart cart = getCartOrThrow(username);

        if (updateQuantityDto.getNewQuantity().equals(0)) {
            removeProductById(cart, updateQuantityDto.getProductId());
            return CartMapper.toDto(cart);
        }

        CartProduct product = getCartProductOrThrow(cart, updateQuantityDto.getProductId());
        if (product.getQuantity().equals(updateQuantityDto.getNewQuantity())) {
            return CartMapper.toDto(cart);
        }

        product.setQuantity(updateQuantityDto.getNewQuantity());
        return CartMapper.toDto(cart);
    }

    private static void removeProductById(ShoppingCart cart, UUID productId) {
        cart.getProducts().removeIf(cp -> cp.getProductId() == productId);
    }

    @Transactional
    public BookedProductsDto bookingProductsFromShoppingCart(String userName) {
        ShoppingCartDto cart = CartMapper.toDto(getCartOrThrow(userName));

        try {
            return warehouseClient.check(cart);
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new NotEnoughProductsException(e.getMessage());
            }
            throw e;
        }
    }

    private ShoppingCart getCartOrThrow(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Shopping cart of user", username));
    }

    private ShoppingCart getOrCreateCart(String username) {
        Optional<ShoppingCart> shoppingCart = repository.findByUsername(username);
        return shoppingCart
                .orElseGet(() -> repository.save(new ShoppingCart(username)));
    }

    private CartProduct getCartProductOrThrow(ShoppingCart cart, UUID productId) {
        return cart.getProducts().stream()
                .filter(cp -> cp.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(NoProductsInShoppingCartException::new);
    }

    private CartProduct getOrCreateCartProduct(ShoppingCart cart, UUID productId) {
        Optional<CartProduct> existingProduct = cart.getProducts().stream()
                .filter(cp -> cp.getProductId().equals(productId))
                .findFirst();

        if (existingProduct.isPresent())
            return existingProduct.get();

        CartProduct newProduct = new CartProduct(cart, productId, 0);
        cart.getProducts().add(newProduct);
        return newProduct;
    }
}
