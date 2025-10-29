package ru.yandex.practicum.commerce.exception;

import java.util.UUID;

public class NotEnoughProductsException extends RuntimeException {

    public NotEnoughProductsException(String message) {
        super(message);
    }

    public NotEnoughProductsException(UUID productId, int requested, int available) {
        super(String.format("Product id:'%s', requested: %d, available: %d", productId, requested, available));
    }
}
