package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.commerce.contract.delivery.order.DeliveryOperations;
import ru.yandex.practicum.commerce.contract.shopping.cart.CartOperations;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(clients = {CartOperations.class, WarehouseOperations.class, DeliveryOperations.class})
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}