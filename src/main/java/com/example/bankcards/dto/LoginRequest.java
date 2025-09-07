package com.example.bankcards.dto;

import lombok.Data;

/**
 * DTO для запроса логина.
 * Содержит username и password.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
