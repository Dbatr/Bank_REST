package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность пользователя системы.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Уникальный идентификатор пользователя (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Уникальное имя пользователя (логин).
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Хэш пароля пользователя.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Полное имя пользователя.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Роли пользователя (например: ADMIN, USER).
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
