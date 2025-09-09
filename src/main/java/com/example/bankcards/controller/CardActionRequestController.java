package com.example.bankcards.controller;

import com.example.bankcards.dto.CardActionRequestCreateDto;
import com.example.bankcards.dto.CardActionRequestDto;
import com.example.bankcards.dto.CardActionRequestUpdateDto;
import com.example.bankcards.service.CardActionRequestService;
import com.example.bankcards.util.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с запросами на блокировку/активацию карт.
 */
@RestController
@RequestMapping("/api/card-action-requests")
@RequiredArgsConstructor
@Tag(name = "Card Action Requests", description = "Запросы на блокировку и активацию карт")
public class CardActionRequestController {

    private final CardActionRequestService service;
    private final AuthUtils authUtils;

    @Operation(summary = "Создать новый запрос на действие с картой (для пользователя)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardActionRequestDto createRequest(@Valid @RequestBody CardActionRequestCreateDto createDto) {
        UUID currentUserId = authUtils.getCurrentUserId();
        return service.createRequest(createDto, currentUserId);
    }

    @Operation(summary = "Получить все запросы (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<CardActionRequestDto> getAllRequests() {
        return service.getAllRequests();
    }

    @Operation(summary = "Получить запрос по ID (для владельца или ADMIN)")
    @GetMapping("/{id}")
    public CardActionRequestDto getRequest(@PathVariable UUID id) {
        UUID currentUserId = authUtils.getCurrentUserId();
        return service.getRequestById(id, currentUserId);
    }

    @Operation(summary = "Обновить статус запроса (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CardActionRequestDto updateRequest(@PathVariable UUID id,
                                              @Valid @RequestBody CardActionRequestUpdateDto updateDto) {
        return service.updateRequestStatus(id, updateDto);
    }
}
