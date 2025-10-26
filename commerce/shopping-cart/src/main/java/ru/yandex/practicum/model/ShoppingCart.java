package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.commerce.dto.ShoppingCartState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class ShoppingCart {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 3, max = 64, message = "Username must be between 3 and 64 characters")
    private String username;

    @Enumerated(EnumType.STRING)
    private ShoppingCartState state = ShoppingCartState.ACTIVE;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CartProduct> products = new ArrayList<>();

    public ShoppingCart(String username) {
        this.username = username;
    }
}