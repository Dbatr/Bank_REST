package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * DTO для запроса создания пользователя.
 * Содержит валидационные аннотации для проверки входных данных.
 */
@Data
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    private Set<String> roles;
}
