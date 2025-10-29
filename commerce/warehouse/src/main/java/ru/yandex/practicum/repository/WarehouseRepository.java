package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WarehouseGood;

import java.util.Collection;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<WarehouseGood, UUID> {

    Collection<WarehouseGood> findAllByProductIdIn(Collection<UUID> ids);
}
