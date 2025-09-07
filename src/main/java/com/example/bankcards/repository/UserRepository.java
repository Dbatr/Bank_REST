package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Поиск пользователя по username.
     *
     * @param username имя пользователя
     * @return Optional с найденным пользователем или пустой
     */
    Optional<User> findByUsername(String username);
}