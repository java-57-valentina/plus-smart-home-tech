package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.OrderProductId;
import ru.yandex.practicum.model.OrderProductInfo;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProductInfo, OrderProductId> {

    Collection<OrderProductInfo> findAllByOrderIdAndProductIdIn(UUID id, Set<UUID> productIds);

    @Modifying
    @Query("DELETE FROM OrderProductInfo opi WHERE opi.orderId = :orderId AND opi.productId IN :productIds")
    void deleteByOrderIdAndProductIdIn(@Param("orderId") UUID orderId,
                                       @Param("productIds") Collection<UUID> productIds);
}
