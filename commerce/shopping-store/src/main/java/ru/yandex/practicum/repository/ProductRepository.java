package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.dto.StoreProductDto;
import ru.yandex.practicum.model.Product;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByCategory(StoreProductDto.ProductCategory productCategory, Pageable pageable);
    Collection<Product> findAllByIdIn(Set<UUID> ids);
}
