package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @GetMapping("/{id}")
    OrderDto getOrder(@PathVariable UUID id);

    @PutMapping
    OrderDto createOrder(@RequestBody NewOrderDto request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody ReturnOrderDto request);


    @PostMapping("/payment")
    PaymentDto processPayment(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    PaymentDto processFailedPayment(@RequestBody @NotNull UUID orderId);


    @PostMapping("/delivery/start")
    void deliveryStarted(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    void delivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    void deliveryFailed(@RequestBody UUID orderId);


    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    PaymentDto calculateTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    PaymentDto calculateDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    PaymentDto processFailedAssembly(@RequestBody UUID orderId);
}