package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CardUpdateRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с банковскими картами.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardMapper::toCardResponse);
    }

    @Override
    public Page<CardResponse> getUserCards(UUID userId, Pageable pageable) {
        return cardRepository.findByOwnerId(userId, pageable)
                .map(cardMapper::toCardResponse);
    }

    @Override
    public List<CardResponse> getAllCardsForAdmin() {
        return cardRepository.findAllWithOwner().stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CardResponse getCardById(UUID id, boolean showFullNumber) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + id));
        if (showFullNumber) {
            return cardMapper.toCardResponseFull(card);
        }
        return cardMapper.toCardResponse(card);
    }

    @Override
    @Transactional
    public CardResponse createCard(CardRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + request.getUserId()));

        Card card = Card.builder()
                .cardNumber(request.getCardNumber())
                .ownerName(request.getOwnerName())
                .expiryDate(request.getExpiryDate())
                .status(request.getStatus())
                .balance(request.getBalance())
                .owner(user)
                .build();

        Card savedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(savedCard);
    }

    @Override
    @Transactional
    public CardResponse updateCard(UUID id, CardUpdateRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + id));

        card.setStatus(request.getStatus());
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(updatedCard);
    }

    @Override
    @Transactional
    public void deleteCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + id));
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public CardResponse blockCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + id));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Карта уже заблокирована");
        }

        card.setStatus(CardStatus.BLOCKED);
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(updatedCard);
    }

    @Override
    @Transactional
    public CardResponse activateCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта не найдена с ID: " + id));

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new IllegalStateException("Карта уже активна");
        }

        if (card.getExpiryDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Нельзя активировать карту с истекшим сроком действия");
        }

        card.setStatus(CardStatus.ACTIVE);
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(updatedCard);
    }
}