package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentDto {
    @JsonProperty("paymentId")
    private UUID id;
    private UUID orderId;

    private double totalPayment;
    private double deliveryTotal;
    private double feeTotal;

    private PaymentState state;
}
