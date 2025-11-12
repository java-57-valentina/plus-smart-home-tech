package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentDto {
    @JsonProperty("paymentId")
    private UUID id;
    private UUID orderId;

    private BigDecimal totalPayment;
    private BigDecimal deliveryTotal;
    private BigDecimal productTotal;

    private PaymentState state;
}
