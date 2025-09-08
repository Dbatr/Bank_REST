package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для запроса создания или обновления карты.
 * Содержит валидационные аннотации для проверки входных данных.
 */
@Data
public class CardRequest {

    @NotBlank(message = "Номер карты обязателен")
    @Pattern(regexp = "^[0-9]{16}$", message = "Номер карты должен содержать 16 цифр")
    private String cardNumber;

    @NotBlank(message = "Имя владельца обязательно")
    private String ownerName;

    @NotNull(message = "Дата окончания обязательна")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDate expiryDate;

    @NotNull(message = "Статус обязателен")
    private CardStatus status;

    @NotNull(message = "Баланс обязателен")
    @DecimalMin(value = "0.0", message = "Баланс не может быть отрицательным")
    private BigDecimal balance;

    @NotNull(message = "ID пользователя обязателен")
    private UUID userId;
}