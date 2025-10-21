package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class StoreController {

    @GetMapping
    String get() {
        return "Not implemented";
    }

    @GetMapping("/{productId}")
    String get(Long id) {
        return "Not implemented";
    }

    @PutMapping
    String put() {
        return "Not implemented";
    }

    @PostMapping
    String post() {
        return "Not implemented";
    }

    @PostMapping("/removeProductFromStore")
    String remove() {
        return "Not implemented";
    }

    @PostMapping("/quantityState")
    String state() {
        return "Not implemented";
    }

}
