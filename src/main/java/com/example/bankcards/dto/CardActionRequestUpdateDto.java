package com.example.bankcards.dto;

import com.example.bankcards.entity.RequestStatus;
import lombok.*;

import jakarta.validation.constraints.NotNull;

/**
 * DTO для обновления статуса запроса админом.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardActionRequestUpdateDto {

    /**
     * Новый статус запроса: APPROVED или REJECTED.
     */
    @NotNull
    private RequestStatus status;
}
