package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.commerce.dto.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private UUID orderId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_from", nullable = false)
    private Address fromAddress;

    @Embedded
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_to", nullable = false)
    private Address toAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;

    @Column(name = "fragile")
    private boolean isFragile;
    private double volume;
    private double weight;
}
