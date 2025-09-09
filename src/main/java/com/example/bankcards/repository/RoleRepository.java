package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Role.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}