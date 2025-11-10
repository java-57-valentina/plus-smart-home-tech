package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

@UtilityClass
public class PaymentMapper {

    public static Payment fromDto(OrderDto order) {
        return Payment.builder()
                .orderId(order.getId())
                .totalPayment(order.getProductsPrice())
//                .deliveryTotal(order.getDeliveryPrice())
//                .feeTotal(order.getTotalPrice())
                .build();
    }

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .state(payment.getState())
                .build();
    }
}
