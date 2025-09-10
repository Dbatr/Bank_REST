package com.example.bankcards.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для ответа после перевода средств между картами.
 * Возвращается клиенту с информацией о результате перевода.
 */
@Data
public class TransferResponse {

    /**
     * Идентификатор карты-отправителя.
     */
    private UUID fromCardId;

    /**
     * Идентификатор карты-получателя.
     */
    private UUID toCardId;

    /**
     * Сумма перевода.
     */
    private BigDecimal amount;

    /**
     * Текущий баланс карты-отправителя после перевода.
     */
    private BigDecimal fromCardBalance;

    /**
     * Текущий баланс карты-получателя после перевода.
     */
    private BigDecimal toCardBalance;
}
