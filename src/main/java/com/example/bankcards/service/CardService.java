package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с банковскими картами.
 * Предоставляет методы для управления картами.
 */
public interface CardService {
    /**
     * Получает все карты с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница с картами
     */
    Page<CardResponse> getAllCards(Pageable pageable);

    /**
     * Получает карты конкретного пользователя.
     *
     * @param userId идентификатор пользователя
     * @param pageable параметры пагинации
     * @return страница с картами пользователя
     */
    Page<CardResponse> getUserCards(UUID userId, Pageable pageable);

    /**
     * Получает все карты для администратора (без пагинации).
     *
     * @return список всех карт
     */
    List<CardResponse> getAllCardsForAdmin();

    /**
     * Получает карту по идентификатору.
     *
     * @param id идентификатор карты
     * @return данные карты
     */
    CardResponse getCardById(UUID id, boolean showFullNumber);

    /**
     * Создает новую карту.
     *
     * @param request данные для создания карты
     * @return созданная карта
     */
    CardResponse createCard(CardRequest request);

    /**
     * Обновляет статус карты.
     *
     * @param id идентификатор карты
     * @param request новые данные карты
     * @return обновленная карта
     */
    CardResponse updateCard(UUID id, CardUpdateRequest request);

    /**
     * Удаляет карту.
     *
     * @param id идентификатор карты для удаления
     */
    void deleteCard(UUID id);

    /**
     * Блокирует карту.
     *
     * @param id идентификатор карты для блокировки
     * @return обновленная карта
     */
    CardResponse blockCard(UUID id);

    /**
     * Активирует карту.
     *
     * @param id идентификатор карты для активации
     * @return обновленная карта
     */
    CardResponse activateCard(UUID id);
}