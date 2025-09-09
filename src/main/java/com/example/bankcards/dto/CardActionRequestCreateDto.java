package com.example.bankcards.dto;

import com.example.bankcards.entity.CardActionType;
import lombok.*;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для создания нового запроса пользователем.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardActionRequestCreateDto {

    /**
     * ID карты, для которой создается запрос.
     */
    @NotNull
    private UUID cardId;

    /**
     * Тип действия: BLOCK или ACTIVATE.
     */
    @NotNull
    private CardActionType actionType;
}
