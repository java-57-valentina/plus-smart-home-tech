package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.shopping.cart.CartOperations;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.*;
import ru.yandex.practicum.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.exception.NotEnoughProductsException;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.exception.IllegalOrderStateException;
import ru.yandex.practicum.exception.OrderValidationException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderProductInfo;
import ru.yandex.practicum.repository.OrderProductRepository;
import ru.yandex.practicum.repository.OrderRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    /*
        Для сервиса order необходимо использовать:

        Сервис delivery:
            planDelivery для создания доставки;
            deliveryCost для расчёта стоимости доставки при общем расчёте стоимости

        Сервис payment:
            productCost для расчёта стоимости товаров;
            getTotalCost для расчёта общей стоимости товаров, доставки и налога;
            payment для запуска процесса оплаты

        Сервис warehouse:
            OK - assemblyProductForOrderFromShoppingCart для сбора заказа по продуктовой корзине;
            getWarehouseAddress для формирования адреса «Откуда», чтобы рассчитать и сохранить «Доставку» (идентификатор).
     */

    private final OrderRepository repository;
    private final OrderProductRepository orderProductRepository;

    private final WarehouseOperations warehouseClient;
    private final CartOperations cartClient;

    public Page<OrderDto> getOrders(String username) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        return repository.findByUsername(username, pageable).map(OrderMapper::toDto);
    }

    public OrderDto getOrderById(UUID id) {
        return OrderMapper.toDto(getOrder(id));
    }

    /*
    1. Корзина не найдена - OK
    2. Неактивная корзина - OK
    3. Неуспешная проверка доступности товаров - OK
    4. Неправильные товары в корзине - OK
    5. Ошибка сохранения заказа в БД (?)
    6. Ошибка сохранения товаров в БД (?)
    7. Ошибка удаления товаров из корзины - OK
    */
    @Transactional
    public OrderDto createOrder(NewOrderDto request) {
        log.debug("create new order {}", request);
        log.debug("check cart {}", request.getCartId());
        try {
            ShoppingCartDto shoppingCartDto = cartClient.getById(request.getCartId());
            validateCart(shoppingCartDto);
            log.debug("check products availability in cart: {}", request.getCartId());
            BookedProductsDto bookedProductsDto = warehouseClient.check(shoppingCartDto);
            Order order = OrderMapper.fromDto(shoppingCartDto, bookedProductsDto);
            order.setProductsPrice(1);
            order.setCreatedAt(Timestamp.from(Instant.now()));
            order = repository.save(order);
            log.debug("saved order: {}", order);
            saveOrderProducts(order, shoppingCartDto.getProducts());
            repository.flush();

            cartClient.remove(order.getUsername(), shoppingCartDto.getProducts().keySet());
            return OrderMapper.toDto(order);
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new NotFoundException(e.getMessage());
            } else if (e.status() == HttpStatus.CONFLICT.value()) {
                throw new NotEnoughProductsException(e.getMessage());
            }
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new OrderValidationException("cannot save order");
        }
    }

    @Transactional
    public OrderDto returnOrder(ReturnOrderDto request) {
        log.debug("return items: {} from order {}", request.getProducts(), request.getOrderId());

        Order order = getOrder(request.getOrderId());
        validateOrderStateForAction(order, "return", Set.of(OrderState.NEW, OrderState.ASSEMBLED, OrderState.ASSEMBLY_FAILED));

        if (order.getState() == OrderState.ASSEMBLED) {
            warehouseClient.returnItems(request.getOrderId(), request.getProducts());
        }

        Collection<OrderProductInfo> orderProducts =
                orderProductRepository.findAllByOrderIdAndProductIdIn(request.getOrderId(), request.getProducts().keySet());
        Collection<UUID> productsToDelete = new ArrayList<>();
        orderProducts.forEach(pi -> {
            Integer decreaseValue = request.getProducts().get(pi.getProductId());
            if (decreaseValue != null) {
                if (pi.getQuantity() < decreaseValue) {
                    throw new IllegalArgumentException(
                            String.format("Cannot return %d items, only %d available for product %s",
                                    decreaseValue, pi.getQuantity(), pi.getProductId())
                    );
                } else if (pi.getQuantity() > decreaseValue) {
                    pi.setQuantity(pi.getQuantity() - decreaseValue);
                } else {
                    productsToDelete.add(pi.getProductId());
                }
            }
        });

        if (!productsToDelete.isEmpty()) {
            log.debug("productsToDelete: {}", productsToDelete);
            orderProductRepository.deleteByOrderIdAndProductIdIn(request.getOrderId(), productsToDelete);
        }
        order = getOrder(request.getOrderId());
        // Не используем статус PRODUCT_RETURNED, чтобы не потерять информацию о том, собран/оплачен ли заказ
        // order.setState(OrderState.PRODUCT_RETURNED);
        return OrderMapper.toDto(order);
    }

    @Transactional
    public PaymentDto processPayment(UUID orderId) {
        return null;
    }

    @Transactional
    public PaymentDto processFailedPayment(UUID orderId) {
        return null;
    }

    @Transactional
    public OrderDto delivery(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        return OrderMapper.toDto(order);
    }

    @Transactional
    public PaymentDto processFailedDelivery(UUID orderId) {
        return null;
    }

    @Transactional
    public PaymentDto completeOrder(UUID orderId) {
        return null;
    }

    public PaymentDto calculateTotal(UUID orderId) {
        return null;
    }

    public PaymentDto calculateDelivery(UUID orderId) {
        return null;
    }

    @Transactional
    public OrderDto assemblyOrder(UUID orderId) {
        log.debug("assembly order: {}", orderId);
        Order order = getOrder(orderId);
        validateOrderStateForAction(order, "assembly", Set.of(OrderState.NEW, OrderState.ASSEMBLY_FAILED));

        Map<UUID, Integer> orderProducts = order.getProducts().stream()
                .collect(Collectors.toMap(
                        OrderProductInfo::getProductId,
                        OrderProductInfo::getQuantity));

        try {
            warehouseClient.assemblyProducts(orderId, orderProducts);
            order.setState(OrderState.ASSEMBLED);
        } catch (FeignException.FeignClientException e) {
            order.setState(OrderState.ASSEMBLY_FAILED);
        }
        return OrderMapper.toDto(order);
    }

    private Order getOrder(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
    }

    private void saveOrderProducts(Order order, Map<UUID, Integer> products) {
        log.debug("saved products {} for orderId: {}", products, order.getId());
        products.forEach((productId, quantity) -> {
            OrderProductInfo productInfo = new OrderProductInfo(order.getId(), productId, quantity);
            order.getProducts().add(productInfo);
        });
    }

    private void validateCart(ShoppingCartDto shoppingCartDto) {
        if (shoppingCartDto.getProducts().isEmpty()) {
            throw new NoProductsInShoppingCartException();
        }

        if (shoppingCartDto.getState() == ShoppingCartState.DEACTIVATED) {
            throw new IllegalStateException("Wrong cart state");
        }
    }

    private void validateOrderStateForAction(Order order, String action, Set<OrderState> allowed_states) {
        if (!allowed_states.contains(order.getState())) {
            throw new IllegalOrderStateException(action, order);
        }
    }
}
