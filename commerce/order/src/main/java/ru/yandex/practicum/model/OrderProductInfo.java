package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_products")
@IdClass(OrderProductId.class)
public class OrderProductInfo {

    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    private int quantity;
}
