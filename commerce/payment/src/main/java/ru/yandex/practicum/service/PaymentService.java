package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository repository;
    private final OrderOperations orderClient;

    public PaymentDto payment(OrderDto request) {
        return null;
    }

    public double totalCost(OrderDto request) {
        return 0;
    }

    public double productCost(UUID orderId) {
        return 0;
    }

    public void refund(UUID orderId) {

    }

    public void failed(UUID orderId) {

    }
}
