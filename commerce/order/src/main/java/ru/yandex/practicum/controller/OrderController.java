package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.NewOrderDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.ReturnOrderDto;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderOperations {
    @Override
    public Page<OrderDto> getUserOrders(String username) {
        log.debug("request for get orders of user: {}", username);
        return null;
    }

    @Override
    public OrderDto createOrder(NewOrderDto request) {
        log.debug("request for create order: {}", request);
        return null;
    }

    @Override
    public PaymentDto returnOrder(ReturnOrderDto request) {
        log.debug("request for return order: {}", request);
        return null;
    }

    @Override
    public PaymentDto processPayment(UUID orderId) {
        log.debug("request for process payment: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto processFailedPayment(UUID orderId) {
        log.debug("request handle failed payment: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto processDelivery(UUID orderId) {
        log.debug("request for process delivery: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto processFailedDelivery(UUID orderId) {
        log.debug("request handle failed delivery: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto completeOrder(UUID orderId) {
        log.debug("request for complete order: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto calculateTotal(UUID orderId) {
        log.debug("request for calculate total: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto calculateDelivery(UUID orderId) {
        log.debug("request for calculate delivery: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto assembleOrder(UUID orderId) {
        log.debug("request for assemble order: {}", orderId);
        return null;
    }

    @Override
    public PaymentDto processFailedAssembly(UUID orderId) {
        log.debug("request handle failed assemble: {}", orderId);
        return null;
    }
}
