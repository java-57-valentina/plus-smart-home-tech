package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.NewOrderDto;
import ru.yandex.practicum.commerce.dto.OrderDto;
import ru.yandex.practicum.commerce.dto.ReturnOrderDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderOperations {

    @GetMapping
    List<OrderDto> getUserOrders(@RequestParam @NotBlank String username);

    @GetMapping("/{id}")
    OrderDto getOrder(@PathVariable UUID id);

    @PutMapping
    OrderDto createOrder(@RequestBody @Valid NewOrderDto request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ReturnOrderDto request);


    @PostMapping("/payment")
    void processPayment(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    void paymentFailed(@RequestBody @NotNull UUID orderId);


    @PostMapping("/delivery/start")
    void deliveryStarted(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    void delivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    void deliveryFailed(@RequestBody UUID orderId);


    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/products")
    OrderDto calculateProductsCost(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly")
    void assembly(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly/failed")
    void assemblyFailed(@RequestBody @NotNull UUID orderId);
}