package ru.yandex.practicum.exception;

import ru.yandex.practicum.model.Order;

public class IllegalOrderStateException extends IllegalStateException {

    public IllegalOrderStateException(String action, Order order) {
        super(String.format("Cannot perform %s for order id: %s, state: %s",
                action, order.getId(), order.getState()));
    }
}
