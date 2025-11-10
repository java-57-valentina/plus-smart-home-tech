package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.contract.shopping.store.StoreOperations;
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

    private final StoreOperations storeClient;
    private final OrderOperations orderClient;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    public WarehouseGoodDtoOut get(UUID productId) {
        return GoodsMapper.toDto(getWarehouseGood(productId));
    }

    public Collection<WarehouseGoodDtoOut> getAll() {
        return repository.findAll().stream()
                .map(GoodsMapper::toDto)
                .toList();
    }

    @Transactional
    public void add(WarehouseGoodDtoIn goodDto) {
        WarehouseGood good = GoodsMapper.fromDto(goodDto);
        WarehouseGood saved = repository.save(good);
        updateQuantityStateInStore(saved);
        log.debug("saved good to store {}", saved);
    }

    @Transactional
    public void updateQuantity(WarehouseGoodQuantityDto request) {
        WarehouseGood good = getWarehouseGood(request.getProductId());
        good.setQuantity(request.getQuantity());
        updateQuantityStateInStore(good);
        log.debug("updated quantity of good {}", good);
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

    public BookedProductsDto check(Map<UUID, Integer> products) {
        log.debug("check goods: {}", products);
        if (products.isEmpty()) {
            log.debug("Nothing to check");
            return new BookedProductsDto();
        }

        Map<UUID, WarehouseGood> warehouseGoods = repository.findAllByProductIdIn(products.keySet()).stream()
                .collect(Collectors.toMap(WarehouseGood::getProductId, Function.identity()));

        if (products.size() > warehouseGoods.size()) {
            Set<UUID> undefinedGoods = products.keySet().stream()
                    .filter(prodId -> !warehouseGoods.containsKey(prodId))
                    .collect(Collectors.toSet());
            log.error("There are {} undefined products in shopping cart: {}", undefinedGoods.size(), undefinedGoods);
            throw new NotFoundException("Products", undefinedGoods);
        }

        double weight = 0.0;
        double volume = 0.0;
        boolean fragile  = false;

        for (Map.Entry<UUID, Integer> cartProduct : products.entrySet()) {
            WarehouseGood good = warehouseGoods.get(cartProduct.getKey());
            if (cartProduct.getValue() > good.getQuantity()) {
                throw new NotEnoughProductsException(good.getProductId(), cartProduct.getValue(), good.getQuantity());
            }
            weight += good.getWeight() * cartProduct.getValue();
            volume += good.getHeight() * good.getWeight() * good.getDepth() * cartProduct.getValue();
            fragile |= good.isFragile();
        }

        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setFragile(fragile);
        bookedProductsDto.setDeliveryWeight(weight);
        bookedProductsDto.setDeliveryVolume(volume);
        return bookedProductsDto;
    }

    @Transactional
    public BookedProductsDto assemblyProducts(UUID orderId, Map<UUID, Integer> products) {
        log.debug("assembly goods: {}", products);
        if (products.isEmpty()) {
            log.debug("Nothing to assembly");
            return new BookedProductsDto();
        }

        Map<UUID, WarehouseGood> warehouseGoods = repository.findAllById(products.keySet()).stream()
                .collect(Collectors.toMap(
                        WarehouseGood::getProductId,
                        Function.identity()));

        BookedProductsDto bookedProductsDto = getBookedProductsDto(products, warehouseGoods);

        for (WarehouseGood warehouseGood : warehouseGoods.values()) {
            int quantity = warehouseGood.getQuantity();
            int needed = products.get(warehouseGood.getProductId());
            if (needed > quantity) {
                orderClient.assemblyFailed(orderId);
                throw new NotEnoughProductsException(warehouseGood.getProductId(), needed, quantity);
            }

            log.debug("change quantity of {}: ({} -> {})", warehouseGood.getProductId(), quantity, quantity - needed);
            changeQuantity(warehouseGood, quantity - needed);
        }

        orderClient.assembly(orderId);
//        OrderBooking orderBooking = orderBookingMapper.mapToOrderBooking(bookedProductsParams, request);
//        orderBooking = orderBookingRepository.save(orderBooking);
//        return orderBookingMapper.mapToBookingDto(orderBooking);

        return bookedProductsDto;
    }

    @Transactional
    public void returnItems(UUID orderId, Map<UUID, Integer> products) {
        log.debug("returnItems: {}", products);
        products.forEach(this::increaseQuantity);
    }

    private WarehouseGood getWarehouseGood(UUID productId) {
        return repository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));
    }

    private void increaseQuantity(UUID productId, Integer increaseValue) {
        WarehouseGood good = getWarehouseGood(productId);
        changeQuantity (good, good.getQuantity() + increaseValue);
    }

    private void changeQuantity(WarehouseGood good, Integer newQuantity) {
        log.debug("updated quantity of good {}: ({} -> {})", good.getProductId(), good.getQuantity(), newQuantity);
        good.setQuantity(newQuantity);
        updateQuantityStateInStore(good);
    }

    private void updateQuantityStateInStore(WarehouseGood good) {
        StoreProductDto.QuantityState state;

        if (good.getQuantity() == 0)
            state = StoreProductDto.QuantityState.ENDED;
        else if (good.getQuantity() < 10)
            state = StoreProductDto.QuantityState.FEW;
        else if (good.getQuantity() < 100)
            state = StoreProductDto.QuantityState.ENOUGH;
        else
            state = StoreProductDto.QuantityState.MANY;

        storeClient.updateQuantityState(good.getProductId(), state);
    }

    private BookedProductsDto getBookedProductsDto(Map<UUID, Integer> products,
                                                   Map<UUID, WarehouseGood> warehouseGoods) {

        log.debug("getBookedProductsDto");

        double weight = 0.0;
        double volume = 0.0;
        boolean fragile  = false;

        for (Map.Entry<UUID, Integer> cartProduct : products.entrySet()) {
            UUID productId = cartProduct.getKey();
            if (!warehouseGoods.containsKey(productId)) {
                throw new NotFoundException("Product", productId);
            }
            WarehouseGood good = warehouseGoods.get(productId);
            if (cartProduct.getValue() > good.getQuantity()) {
                throw new NotEnoughProductsException(good.getProductId(), cartProduct.getValue(), good.getQuantity());
            }
            weight += good.getWeight() * cartProduct.getValue();
            volume += good.getHeight() * good.getWeight() * good.getDepth() * cartProduct.getValue();
            fragile |= good.isFragile();
        }

        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setFragile(fragile);
        bookedProductsDto.setDeliveryWeight(weight);
        bookedProductsDto.setDeliveryVolume(volume);

        return bookedProductsDto;
    }
}
