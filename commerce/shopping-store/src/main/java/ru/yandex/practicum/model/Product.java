package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.commerce.dto.StoreProductDto;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name")
    private String productName;

    private String description;
    private Double price;

    @Column(name = "image_src")
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    private StoreProductDto.ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state")
    private StoreProductDto.QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private StoreProductDto.ProductState state;
}
