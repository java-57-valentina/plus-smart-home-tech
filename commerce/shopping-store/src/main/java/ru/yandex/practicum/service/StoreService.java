package ru.yandex.practicum.service;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    public StoreProductDto get(String productId) {
        Product product = repository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product", productId));
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
        log.debug("UUID: '{}'", uuid);
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
        log.debug("UUID: '{}'", uuid);
        Product product = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product", uuid));
        product.setState(StoreProductDto.ProductState.DEACTIVATE);
        return true;
    }

    @Transactional
    public boolean updateQuantityState(UUID productId, StoreProductDto.QuantityState quantityState) {
        log.debug("UUID: '{}'", productId);
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));
        product.setQuantityState(quantityState);
        return true;
    }
}
