package ru.yandex.practicum.commerce.contract.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentOperations {

    // Формирование оплаты для заказа (переход в платежный шлюз)
    @PostMapping
    PaymentDto payment(@RequestBody @Valid OrderDto request);

    // Расчёт полной стоимости заказа
    @PostMapping("/totalCost")
    BigDecimal totalCost(@RequestBody @Valid OrderDto request);

    // Расчёт стоимости товаров в заказе.
    @PostMapping("/productCost")
    BigDecimal getProductCost(@RequestBody @Valid OrderDto orderDto);

    // Метод для эмуляции успешной оплаты в платежного шлюза
    @PostMapping("/refund")
    void refund(@RequestBody @NotNull UUID paymentId);

    // Метод для эмуляции отказа в оплате платежного шлюза
    @PostMapping("/failed")
    void failed(@RequestBody @NotNull UUID paymentId);
}