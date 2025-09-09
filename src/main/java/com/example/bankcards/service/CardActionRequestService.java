package com.example.bankcards.service;

import com.example.bankcards.dto.CardActionRequestCreateDto;
import com.example.bankcards.dto.CardActionRequestDto;
import com.example.bankcards.dto.CardActionRequestUpdateDto;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с запросами на действия с картой.
 */
public interface CardActionRequestService {

    /**
     * Создать новый запрос пользователем.
     */
    CardActionRequestDto createRequest(CardActionRequestCreateDto createDto, UUID currentUserId);

    /**
     * Получить все запросы для админа.
     */
    List<CardActionRequestDto> getAllRequests();

    /**
     * Получить запрос по ID (для владельца карты или админа).
     */
    CardActionRequestDto getRequestById(UUID id, UUID currentUserId);

    /**
     * Обновить статус запроса админом (APPROVED/REJECTED).
     */
    CardActionRequestDto updateRequestStatus(UUID id, CardActionRequestUpdateDto updateDto);
}
