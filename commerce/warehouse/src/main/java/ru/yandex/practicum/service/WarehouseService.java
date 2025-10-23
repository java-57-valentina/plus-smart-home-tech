package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.WarehouseAddressDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodDto;
import ru.yandex.practicum.commerce.dto.WarehouseGoodQuantityDto;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.GoodsMapper;
import ru.yandex.practicum.model.WarehouseGood;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

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
        repository.save(good);
    }

    @Transactional
    public void updateQuantity(WarehouseGoodQuantityDto request) {

        WarehouseGood product = repository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product", request.getProductId()));
        product.setQuantity(request.getQuantity());
    }

    public WarehouseAddressDto getAddress() {
        return WarehouseAddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    public boolean check(ShoppingCartDto cardRequestDto) {
        Map<UUID, Integer> requestedProducts = cardRequestDto.getProducts();
        if (requestedProducts.isEmpty())
            return true;

        Set<UUID> productIds = new HashSet<>(requestedProducts.keySet());
        Collection<WarehouseGood> goods = repository.findAllByProductIdIn(productIds);
        if (goods.size() < productIds.size()) {
            return false;
        }

        return goods.stream()
                .allMatch(g -> g.getQuantity() >= requestedProducts.get(g.getProductId()));
    }
}
