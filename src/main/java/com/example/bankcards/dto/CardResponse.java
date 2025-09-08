package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для ответа с данными карты.
 * Содержит маскированный номер карты и информацию о владельце.
 */
@Data
public class CardResponse {
    private UUID id;
    private String maskedCardNumber;
    private String ownerName;
    private LocalDate expiryDate;
    private CardStatus status;
    private BigDecimal balance;
    private UUID userId;
    private String userFullName;
}