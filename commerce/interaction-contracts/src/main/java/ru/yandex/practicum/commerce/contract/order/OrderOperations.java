package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.NewOrderDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.PaymentDto;
import ru.yandex.practicum.commerce.dto.ReturnOrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderOperations {

    @GetMapping
    Page<OrderDto> getUserOrders(@RequestParam @NotBlank String username);

    @PutMapping
    OrderDto createOrder(@RequestBody NewOrderDto request);

    @PostMapping("/return")
    PaymentDto returnOrder(@RequestBody ReturnOrderDto request);

    @PostMapping("/payment")
    PaymentDto processPayment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    PaymentDto processFailedPayment(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    PaymentDto processDelivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    PaymentDto processFailedDelivery(@RequestBody UUID orderId);

    @PostMapping("/completed")
    PaymentDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    PaymentDto calculateTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    PaymentDto calculateDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    PaymentDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    PaymentDto processFailedAssembly(@RequestBody UUID orderId);
}