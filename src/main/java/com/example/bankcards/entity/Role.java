package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность роли пользователя.
 * Определяет права доступа (например: ADMIN, USER).
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /**
     * Имя роли (используется как первичный ключ).
     */
    @Id
    private String name;
}
