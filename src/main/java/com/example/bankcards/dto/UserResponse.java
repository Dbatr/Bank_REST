package com.example.bankcards.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

/**
 * DTO для отображения информации о пользователе.
 */
@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String fullName;
    private Set<String> roles;
}
