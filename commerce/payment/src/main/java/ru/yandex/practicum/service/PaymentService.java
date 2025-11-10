package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.contract.shopping.store.StoreOperations;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.PaymentState;
import ru.yandex.practicum.commerce.dto.StoreProductDto;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository repository;
    private final OrderOperations orderClient;
    private final StoreOperations storeClient;

    private static final double VAT_RATE = 0.20;

    @Transactional
    public PaymentDto payment(OrderDto order) {
        log.debug("create payment");
        Payment payment = PaymentMapper.fromDto(order);
        payment.setState(PaymentState.PENDING);
        payment = repository.save(payment);
        return PaymentMapper.toDto(payment);
    }

    public double totalCost(OrderDto order) {
        // TODO:
        return 0;
    }

    public double productCost(OrderDto orderDto) {
        double totalProductsCost = 0;
        Map<UUID, Integer> products = orderDto.getProducts();

        // TODO: получать список продуктов одним запросом
        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            StoreProductDto product = storeClient.get(entry.getKey());
            totalProductsCost += product.getPrice() * entry.getValue();
        }
        return totalProductsCost;
    }

    @Transactional
    public void refund(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.SUCCESS);
        orderClient.processPayment(payment.getOrderId());
    }

    @Transactional
    public void failed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.FAILED);
        orderClient.paymentFailed(payment.getOrderId());
    }

    private Payment getPayment(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment", id));
    }
    private void validatePaymentInfo(Double... prices) {
        for (Double price : prices) {
            if (price == null || price == 0) {
                throw new NotEnoughInfoInOrderToCalculateException("Not enough payment info in order");
            }
        }
    }
}
