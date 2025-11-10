package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.payment.PaymentOperations;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentOperations {

    private final PaymentService service;

    @Override
    public PaymentDto payment(@RequestBody @Valid OrderDto request) {
        log.debug("request for create payment for {}", request);
        return service.payment(request);
    }

    @Override
    public double totalCost(OrderDto request) {
        log.debug("request ");
        return service.totalCost(request);
    }

    @Override
    public double productCost(OrderDto orderDto) {
        log.debug("request for products cost");
        return service.productCost(orderDto);
    }

    @Override
    public void refund(UUID paymentId) {
        log.debug("request ");
        service.refund(paymentId);
    }

    @Override
    public void failed(UUID paymentId) {
        log.debug("request ");
        service.failed(paymentId);
    }
}