package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.contract.shopping.store.StoreOperations;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.PaymentState;
import ru.yandex.practicum.commerce.exception.ConflictException;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.commerce.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
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

    private static final double VAT_MULTIPLIER = 1.1;

    @Transactional
    public PaymentDto payment(OrderDto order) {
        log.debug("create payment");
        Payment payment = PaymentMapper.fromDto(order);
        payment.setState(PaymentState.PENDING);
        payment = repository.save(payment);
        return PaymentMapper.toDto(payment);
    }

    public BigDecimal totalCost(OrderDto orderDto) {
        validatePaymentInfo(orderDto.getDeliveryPrice(), orderDto.getProductsPrice());

        return orderDto.getProductsPrice()
                .multiply(BigDecimal.valueOf(VAT_MULTIPLIER))
                .add(orderDto.getDeliveryPrice());
    }

    public BigDecimal getProductCost(OrderDto orderDto) {
        BigDecimal totalProductsCost = BigDecimal.ZERO;
        Map<UUID, Integer> products = orderDto.getProducts();
        Map<UUID, BigDecimal> prices;

        try {
            prices = storeClient.getProductPrices(products.keySet());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new NotFoundException(e.getMessage());
            }
            throw e;
        }

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            BigDecimal price = prices.get(entry.getKey());
            BigDecimal amount = BigDecimal.valueOf(entry.getValue());
            totalProductsCost = totalProductsCost.add(price.multiply(amount));
        }
        return totalProductsCost;
    }

    @Transactional
    public void refund(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.SUCCESS);
        try {
            orderClient.processPayment(payment.getOrderId());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new ConflictException(e.getMessage());
            }
            throw e;
        }
    }

    @Transactional
    public void failed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.FAILED);
        try {
            orderClient.paymentFailed(payment.getOrderId());
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new ConflictException(e.getMessage());
            }
            throw e;
        }
    }

    private Payment getPayment(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment", id));
    }
    private void validatePaymentInfo(BigDecimal... prices) {
        for (BigDecimal price : prices) {
            if (price == null || price.equals(BigDecimal.ZERO)) {
                throw new NotEnoughInfoInOrderToCalculateException("Not enough payment info in order");
            }
        }
    }
}
