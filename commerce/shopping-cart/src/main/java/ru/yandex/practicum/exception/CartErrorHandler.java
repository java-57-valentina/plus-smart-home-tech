package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorHandler;
import ru.yandex.practicum.commerce.exception.ErrorResponse;
import ru.yandex.practicum.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.exception.NotEnoughProductsException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CartErrorHandler extends ErrorHandler {

    @ExceptionHandler(NotEnoughProductsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotEnoughProductsException(NotEnoughProductsException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason("Not Enough Products on warehouse")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason("NoProductsInShoppingCart")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
