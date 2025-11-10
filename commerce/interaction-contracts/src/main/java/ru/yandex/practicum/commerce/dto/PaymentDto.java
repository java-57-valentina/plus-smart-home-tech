package ru.yandex.practicum.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {
    @JsonProperty("paymentId")
    private UUID id;
    private double totalPayment;
    private double deliveryTotal;
    private double feeTotal;
}
