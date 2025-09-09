package com.example.bankcards.dto;

import lombok.Data;

import java.util.Set;

/**
 * DTO для обновления информации о пользователе.
 */
@Data
public class UserUpdateRequest {
    private String fullName;
    private Set<String> roles;
}
