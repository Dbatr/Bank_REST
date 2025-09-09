package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardActionRequestCreateDto;
import com.example.bankcards.dto.CardActionRequestDto;
import com.example.bankcards.dto.CardActionRequestUpdateDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardActionRequest;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardActionRequestMapper;
import com.example.bankcards.repository.CardActionRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardActionRequestService;
import com.example.bankcards.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с запросами на блокировку/активацию карт.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardActionRequestServiceImpl implements CardActionRequestService {

    private final CardActionRequestRepository requestRepository;
    private final CardRepository cardRepository;
    private final CardActionRequestMapper mapper;
    private final AuthUtils authUtils;

    @Override
    @Transactional
    public CardActionRequestDto createRequest(CardActionRequestCreateDto createDto, UUID currentUserId) {
        Card card = cardRepository.findById(createDto.getCardId())
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + createDto.getCardId()));

        if (!card.getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Вы не можете создавать запросы для чужих карт");
        }

        CardActionRequest request = CardActionRequest.builder()
                .card(card)
                .actionType(createDto.getActionType())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return mapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<CardActionRequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardActionRequestDto getRequestById(UUID id, UUID currentUserId) {
        CardActionRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос не найден с ID: " + id));

        if (!authUtils.isAdmin() && !request.getCard().getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Доступ запрещен: вы не являетесь владельцем этой карты");
        }

        return mapper.toDto(request);
    }

    @Override
    @Transactional
    public CardActionRequestDto updateRequestStatus(UUID id, CardActionRequestUpdateDto updateDto) {
        CardActionRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос не найден с ID: " + id));

        request.setStatus(updateDto.getStatus());
        request.setProcessedAt(LocalDateTime.now());

        if (updateDto.getStatus() == RequestStatus.APPROVED) {
            Card card = request.getCard();
            card.setStatus(request.getActionType() == com.example.bankcards.entity.CardActionType.BLOCK
                    ? CardStatus.BLOCKED
                    : CardStatus.ACTIVE);
        }

        return mapper.toDto(requestRepository.save(request));
    }
}
