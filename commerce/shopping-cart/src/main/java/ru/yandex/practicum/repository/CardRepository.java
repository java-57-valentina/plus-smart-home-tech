package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<ShoppingCart, UUID> {
    Optional<ShoppingCart> findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
