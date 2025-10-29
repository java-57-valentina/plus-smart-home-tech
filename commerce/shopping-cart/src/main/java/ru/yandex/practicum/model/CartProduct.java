package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_products")
public class CartProduct {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @ToString.Exclude
    private ShoppingCart cart;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    private Integer quantity;

    public CartProduct(ShoppingCart cart, UUID productId, Integer quantity) {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }
}
