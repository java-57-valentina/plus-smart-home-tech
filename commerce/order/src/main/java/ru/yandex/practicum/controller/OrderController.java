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
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderOperations {

    private final OrderService service;

    // TODO: добавить параметры пагинации
    @Override
    public Page<OrderDto> getUserOrders(String username) {
        log.debug("request for get orders of user: {}", username);
        return service.getOrders(username);
    }

    @Override
    public OrderDto getOrder(UUID id) {
        log.debug("request for get order by id: {}", id);
        return service.getOrderById(id);
    }

    @Override
    public OrderDto createOrder(NewOrderDto request) {
        log.debug("request for create order: {}", request);
        OrderDto created = service.createOrder(request);
        log.debug("return created order: {}", created);
        return created;
    }

    @Override
    public OrderDto returnOrder(ReturnOrderDto request) {
        log.debug("request for return order: {}", request);
        return service.returnOrder(request);
    }

    @Override
    public PaymentDto processPayment(UUID orderId) {
        log.debug("request for process payment: {}", orderId);
        return service.processPayment(orderId);
    }

    @Override
    public PaymentDto processFailedPayment(UUID orderId) {
        log.debug("request handle failed payment: {}", orderId);
        return service.processFailedPayment(orderId);
    }


    @Override
    public void deliveryStarted(UUID orderId) {
        log.debug("request for handle start delivery of: {}", orderId);
        service.deliveryStarted(orderId);
    }

    @Override
    public void delivery(UUID orderId) {
        log.debug("request for process delivery: {}", orderId);
        service.deliverySuccess(orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        log.debug("request handle failed delivery: {}", orderId);
        service.deliveryFailed(orderId);
    }


    @Override
    public OrderDto completeOrder(UUID orderId) {
        log.debug("request for complete order: {}", orderId);
        return service.completeOrder(orderId);
    }

    @Override
    public PaymentDto calculateTotal(UUID orderId) {
        log.debug("request for calculate total: {}", orderId);
        return service.calculateTotal(orderId);
    }

    @Override
    public PaymentDto calculateDelivery(UUID orderId) {
        log.debug("request for calculate delivery: {}", orderId);
        return service.calculateDelivery(orderId);
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        log.debug("request for assemble order: {}", orderId);
        return service.assemblyOrder(orderId);
    }

    @Override
    public PaymentDto processFailedAssembly(UUID orderId) {
        log.debug("STUB: request handle failed assemble: {}", orderId);
        return null;
    }
}
