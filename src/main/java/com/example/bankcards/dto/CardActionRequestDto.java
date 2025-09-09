package com.example.bankcards.dto;

import com.example.bankcards.entity.CardActionType;
import com.example.bankcards.entity.RequestStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для передачи данных запроса на блокировку/активацию карты.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardActionRequestDto {

    /**
     * Уникальный идентификатор запроса.
     */
    private UUID id;

    /**
     * ID карты, для которой создается запрос.
     */
    private UUID cardId;

    /**
     * ID владельца карты.
     */
    private UUID cardOwnerId;

    /**
     * Тип действия: BLOCK или ACTIVATE.
     */
    private CardActionType actionType;

    /**
     * Статус запроса: PENDING, APPROVED, REJECTED.
     */
    private RequestStatus status;

    /**
     * Дата и время создания запроса.
     */
    private LocalDateTime createdAt;

    /**
     * Дата и время обработки запроса админом.
     */
    private LocalDateTime processedAt;
}
