package ru.yandex.practicum.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.StoreProductDto;
import ru.yandex.practicum.commerce.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final ProductRepository repository;

    public Page<StoreProductDto> get(StoreProductDto.ProductCategory category, int page, int size, String sort) {

        Sort sortConfig = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortConfig);

        Page<Product> productsPage = repository.findByCategory(category, pageable);

        return productsPage.map(ProductMapper::toDto);
    }

    private static Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "name");
        }

        try {
            // Поддерживаемые форматы: "price,asc", "name,desc", "price"
            String[] sortParams = sort.split(",");
            String field = sortParams[0].trim();

            List<String> allowedFields = Arrays.asList("productName", "price");
            if (!allowedFields.contains(field)) {
                throw new IllegalArgumentException("Invalid sort field: " + field + ". Allowed fields: " + allowedFields);
            }

            if (sortParams.length == 1) {
                return Sort.by(Sort.Direction.ASC, field);
            } else {
                Sort.Direction direction = Sort.Direction.fromString(sortParams[1].trim());
                return Sort.by(direction, field);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid sort parameter: " + sort +
                    ". Expected format: 'field,direction' or 'field'");
        }
    }

    public StoreProductDto get(UUID productId) {
        Product product = getProduct(productId);
        return ProductMapper.toDto(product);
    }

    @Transactional
    public StoreProductDto add(StoreProductDto productDto) {
        Product product = ProductMapper.fromDto(productDto);
        Product saved = repository.save(product);
        return ProductMapper.toDto(saved);
    }

    @Transactional
    public StoreProductDto update(StoreProductDto productDto) {
        UUID uuid = UUID.fromString(productDto.getId().replace("\"", ""));
        Product product = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product", uuid));

        Optional.ofNullable(productDto.getName()).ifPresent(product::setProductName);
        Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDto.getCategory()).ifPresent(product::setCategory);
        Optional.ofNullable(productDto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productDto.getImageSrc()).ifPresent(product::setImageSrc);
        Optional.ofNullable(productDto.getState()).ifPresent(product::setState);
        Optional.ofNullable(productDto.getQuantityState()).ifPresent(product::setQuantityState);
        return ProductMapper.toDto(product);
    }

    @Transactional
    public boolean deactivate(String productId) {
        UUID uuid = UUID.fromString(productId.replace("\"", ""));
        Product product = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product", uuid));
        product.setState(StoreProductDto.ProductState.DEACTIVATE);
        return true;
    }

    @Transactional
    public boolean updateQuantityState(UUID productId, StoreProductDto.QuantityState quantityState) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));
        product.setQuantityState(quantityState);
        return true;
    }

    private Product getProduct(UUID productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));
    }

    public Map<UUID, Double> getProductPrices(@NotNull Set<UUID> uuids) {
        Collection<Product> allByIdIn = repository.findAllByIdIn(uuids);

        Map<UUID, Double> prices = repository.findAllByIdIn(uuids)
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        if (uuids.size() > prices.size()) {
            uuids.removeAll(prices.keySet());
            throw new NotFoundException("Products not found: [{}]", uuids);
        }
        return prices;
    }
}
