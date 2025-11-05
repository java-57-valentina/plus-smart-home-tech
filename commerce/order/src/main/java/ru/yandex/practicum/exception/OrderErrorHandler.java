package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorHandler;
import ru.yandex.practicum.commerce.exception.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class OrderErrorHandler extends ErrorHandler {

    @ExceptionHandler(IllegalOrderStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalOrderStateException(IllegalOrderStateException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .reason("Illegal state.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(OrderValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOrderValidationException(OrderValidationException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .reason("Invalid order data")
                .timestamp(LocalDateTime.now())
                .build();
    }


}
