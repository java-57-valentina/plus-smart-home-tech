package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.exception.NotEnoughProductsException;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.GoodsMapper;
import ru.yandex.practicum.model.WarehouseGood;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseService {

    private final WarehouseRepository repository;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Transactional
    public void add(WarehouseGoodDto goodDto) {
        WarehouseGood good = GoodsMapper.fromDto(goodDto);
        WarehouseGood saved = repository.save(good);
        log.debug("saved good to store {}", saved);
    }

    @Transactional
    public void updateQuantity(WarehouseGoodQuantityDto request) {
        WarehouseGood product = repository
                .findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product", request.getProductId()));
        product.setQuantity(request.getQuantity());
        log.debug("updated quantity of good {}", product);
    }

    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    public BookedProductsDto check(ShoppingCartDto cartRequestDto) {
        Map<UUID, Integer> cartProducts = cartRequestDto.getProducts();
        if (cartProducts.isEmpty()) {
            log.debug("Shopping cart is empty, nothing to check");
            return new BookedProductsDto();
        }

        Map<UUID, WarehouseGood> warehouseGoods = repository.findAllByProductIdIn(cartProducts.keySet()).stream()
                .collect(Collectors.toMap(WarehouseGood::getProductId, Function.identity()));

        if (cartProducts.size() > warehouseGoods.size()) {
            Set<UUID> undefinedGoods = cartProducts.keySet().stream()
                    .filter(prodId -> !warehouseGoods.containsKey(prodId))
                    .collect(Collectors.toSet());
            log.debug("There are {} undefined products in shopping cart: {}", undefinedGoods.size(), undefinedGoods);
            throw new NotFoundException("Products", undefinedGoods);
        }

        double weight = 0.0;
        double volume = 0.0;
        boolean fragile  = false;

        for (Map.Entry<UUID, Integer> cartProduct : cartProducts.entrySet()) {
            WarehouseGood product = warehouseGoods.get(cartProduct.getKey());
            if (cartProduct.getValue() > product.getQuantity()) {
                throw new NotEnoughProductsException(product.getProductId(), cartProduct.getValue(), product.getQuantity());
            }
            weight += product.getWeight() * cartProduct.getValue();
            volume += product.getHeight() * product.getWeight() * product.getDepth() * cartProduct.getValue();
            fragile |= product.isFragile();
        }

        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setFragile(fragile);
        bookedProductsDto.setDeliveryWeight(weight);
        bookedProductsDto.setDeliveryVolume(volume);
        return bookedProductsDto;
    }
}
