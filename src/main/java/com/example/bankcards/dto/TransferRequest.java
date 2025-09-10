package com.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для входящего запроса на перевод между картами.
 * Используется клиентом при инициировании перевода.
 */
@Data
public class TransferRequest {

    /**
     * Идентификатор карты-отправителя.
     */
    @NotNull
    private UUID fromCardId;

    /**
     * Идентификатор карты-получателя.
     */
    @NotNull
    private UUID toCardId;

    /**
     * Сумма перевода. Должна быть больше 0.
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    private BigDecimal amount;

}
