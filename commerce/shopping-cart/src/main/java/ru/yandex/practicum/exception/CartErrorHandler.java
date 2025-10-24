package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorHandler;
import ru.yandex.practicum.commerce.exception.ErrorResponse;
import ru.yandex.practicum.commerce.exception.NoProductsInShoppingCartException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CartErrorHandler extends ErrorHandler {


    @ExceptionHandler(NotEnoughProductsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotEnoughProductsException(NotEnoughProductsException ex) {
        return ErrorResponse.builder()
                .message("Not Enough Products")
                .reason("Missing parameter.")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException ex) {
        return ErrorResponse.builder()
                .message("Not Enough Products")
                .reason("Missing parameter.")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
