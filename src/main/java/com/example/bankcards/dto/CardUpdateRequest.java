package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для запроса обновления статуса карты.
 */
@Data
public class CardUpdateRequest {

    @NotNull(message = "Статус обязателен")
    private CardStatus status;
}