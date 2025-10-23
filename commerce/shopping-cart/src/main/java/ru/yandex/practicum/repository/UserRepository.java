package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
