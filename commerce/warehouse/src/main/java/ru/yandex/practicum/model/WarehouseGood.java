package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "goods")
public class WarehouseGood {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private boolean fragile;
    private double weight;

    private double width;
    private double height;
    private double depth;

    private int quantity;
    private int reserved;

    public int getAvailable() {
        return quantity - reserved;
    }
}
