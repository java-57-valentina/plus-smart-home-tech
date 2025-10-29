package ru.yandex.practicum.commerce.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, Object id) {
        super(entity + " with id: " + id + " not found");
    }
}
