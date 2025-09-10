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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private User user;
    private UUID cardId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        cardId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .fullName("Test User")
                .build();

        card = Card.builder()
                .id(cardId)
                .cardNumber("1234567890123456")
                .owner(user)
                .ownerName(user.getFullName())
                .balance(BigDecimal.valueOf(1000))
                .expiryDate(LocalDate.now().plusYears(1))
                .status(CardStatus.ACTIVE)
                .build();
    }

    @Test
    void createCard_Success() {
        CardRequest request = new CardRequest();
        request.setUserId(userId);
        request.setCardNumber("1234567890123456");
        request.setOwnerName(user.getFullName());
        request.setBalance(BigDecimal.valueOf(1000));
        request.setExpiryDate(LocalDate.now().plusYears(1));
        request.setStatus(CardStatus.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toCardResponse(any(Card.class))).thenReturn(new CardResponse());

        CardResponse response = cardService.createCard(request);

        assertNotNull(response);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_UserNotFound() {
        CardRequest request = new CardRequest();
        request.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.createCard(request));
        verify(cardRepository, never()).save(any());
    }

    @Test
    void updateCard_Success() {
        CardUpdateRequest updateRequest = new CardUpdateRequest();
        updateRequest.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toCardResponse(card)).thenReturn(new CardResponse());

        CardResponse response = cardService.updateCard(cardId, updateRequest);

        assertNotNull(response);
        assertEquals(CardStatus.BLOCKED, card.getStatus());
    }

    @Test
    void updateCard_NotFound() {
        CardUpdateRequest updateRequest = new CardUpdateRequest();
        updateRequest.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.updateCard(cardId, updateRequest));
    }

    @Test
    void blockCard_Success() {
        card.setStatus(CardStatus.ACTIVE);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toCardResponse(card)).thenReturn(new CardResponse());

        CardResponse response = cardService.blockCard(cardId);

        assertNotNull(response);
        assertEquals(CardStatus.BLOCKED, card.getStatus());
    }

    @Test
    void blockCard_AlreadyBlocked() {
        card.setStatus(CardStatus.BLOCKED);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, () -> cardService.blockCard(cardId));
    }

    @Test
    void activateCard_Success() {
        card.setStatus(CardStatus.BLOCKED);
        card.setExpiryDate(LocalDate.now().plusDays(1));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toCardResponse(card)).thenReturn(new CardResponse());

        CardResponse response = cardService.activateCard(cardId);

        assertNotNull(response);
        assertEquals(CardStatus.ACTIVE, card.getStatus());
    }

    @Test
    void activateCard_AlreadyActive() {
        card.setStatus(CardStatus.ACTIVE);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, () -> cardService.activateCard(cardId));
    }

    @Test
    void activateCard_Expired() {
        card.setStatus(CardStatus.BLOCKED);
        card.setExpiryDate(LocalDate.now().minusDays(1));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, () -> cardService.activateCard(cardId));
    }
}
