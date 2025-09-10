package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CardUpdateRequest;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления банковскими картами.
 * Предоставляет эндпоинты для CRUD операций с картами.
 */
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "Управление банковскими картами")
public class CardController {
    private final CardService cardService;
    private final AuthUtils authUtils;

    @Operation(summary = "Получить все карты (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<CardResponse> getAllCardsForAdmin() {
        return cardService.getAllCardsForAdmin();
    }

    @Operation(summary = "Получить все карты с пагинацией (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Page<CardResponse> getAllCards(@PageableDefault(size = 20) Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @Operation(summary = "Получить карты текущего пользователя")
    @GetMapping("/my")
    public Page<CardResponse> getMyCards(@PageableDefault(size = 10) Pageable pageable) {
        UUID currentUserId = authUtils.getCurrentUserId();
        return cardService.getUserCards(currentUserId, pageable);
    }

    @Operation(summary = "Получить карту по ID (доступно владельцу карты или ADMIN)")
    @GetMapping("/{id}")
    public CardResponse getCard(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean showFullNumber
    ) {
        CardResponse card = cardService.getCardById(id, showFullNumber);
        if (!authUtils.isCardOwnerOrAdmin(card.getUserId())) {
            throw new SecurityException("Доступ запрещен: вы не являетесь владельцем этой карты");
        }
        return card;
    }

    @Operation(summary = "Создать новую карту (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse createCard(@Valid @RequestBody CardRequest request) {
        return cardService.createCard(request);
    }

    @Operation(summary = "Обновить статус карты (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CardResponse updateCard(@PathVariable UUID id, @Valid @RequestBody CardUpdateRequest request) {
        return cardService.updateCard(id, request);
    }

    @Operation(summary = "Удалить карту (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
    }

    @Operation(summary = "Заблокировать карту (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/block")
    public CardResponse blockCard(@PathVariable UUID id) {
        return cardService.blockCard(id);
    }

    @Operation(summary = "Активировать карту (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/activate")
    public CardResponse activateCard(@PathVariable UUID id) {
        return cardService.activateCard(id);
    }
}
