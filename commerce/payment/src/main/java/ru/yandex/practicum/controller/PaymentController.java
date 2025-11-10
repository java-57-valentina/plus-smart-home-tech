package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/v1/delivery")
public class PaymentController implements PaymentOperations {

    private final PaymentService service;


    @Override
    public PaymentDto payment(OrderDto request) {
        log.debug("request ");
        return service.payment(request);
    }

    @Override
    public double totalCost(OrderDto request) {
        log.debug("request ");
        return service.totalCost(request);
    }

    @Override
    public double productCost(UUID orderId) {
        log.debug("request ");
        return service.productCost(orderId);
    }

    @Override
    public void refund(UUID orderId) {
        log.debug("request ");
        service.refund(orderId);
    }

    @Override
    public void failed(UUID orderId) {
        log.debug("request ");
        service.failed(orderId);
    }
}