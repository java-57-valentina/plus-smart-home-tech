package ru.yandex.practicum.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorHandler;

@RestControllerAdvice
public class CartErrorHandler extends ErrorHandler {

}
