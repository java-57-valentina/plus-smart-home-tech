package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;

@EnableFeignClients(clients = {WarehouseOperations.class})
@SpringBootApplication
@EnableDiscoveryClient
public class ShoppingCartApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApp.class, args);
    }
}