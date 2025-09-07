package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для ответа на успешную аутентификацию.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    /**
     * JWT токен для дальнейшей авторизации.
     */
    private String token;
}
