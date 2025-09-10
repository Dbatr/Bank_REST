package com.example.bankcards.service.impl;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AuthUtils authUtils;

    @InjectMocks
    private TransferServiceImpl transferService;

    private User user;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");

        card1 = new Card();
        card1.setId(UUID.randomUUID());
        card1.setOwner(user);
        card1.setBalance(new BigDecimal("1000"));

        card2 = new Card();
        card2.setId(UUID.randomUUID());
        card2.setOwner(user);
        card2.setBalance(new BigDecimal("500"));
    }

    @Test
    void transfer_Successful() {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(card1.getId());
        request.setToCardId(card2.getId());
        request.setAmount(new BigDecimal("200"));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));
        when(cardRepository.findById(card2.getId())).thenReturn(Optional.of(card2));
        when(authUtils.isCardOwnerOrAdmin(user.getId())).thenReturn(true);

        TransferResponse response = transferService.transfer(request);

        assertEquals(new BigDecimal("800"), response.getFromCardBalance());
        assertEquals(new BigDecimal("700"), response.getToCardBalance());
        assertEquals(request.getAmount(), response.getAmount());

        verify(cardRepository, times(1)).save(card1);
        verify(cardRepository, times(1)).save(card2);
    }

    @Test
    void transfer_SameCard_ThrowsException() {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(card1.getId());
        request.setToCardId(card1.getId());
        request.setAmount(new BigDecimal("100"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(request));
        assertEquals("Нельзя перевести на ту же самую карту", ex.getMessage());
    }

    @Test
    void transfer_DifferentOwners_ThrowsException() {
        Card otherCard = new Card();
        otherCard.setId(UUID.randomUUID());
        otherCard.setOwner(new User());
        otherCard.setBalance(new BigDecimal("100"));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(card1.getId());
        request.setToCardId(otherCard.getId());
        request.setAmount(new BigDecimal("100"));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));
        when(cardRepository.findById(otherCard.getId())).thenReturn(Optional.of(otherCard));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> transferService.transfer(request));
        assertEquals("Перевод возможен только между своими картами", ex.getMessage());
    }

    @Test
    void transfer_NotEnoughBalance_ThrowsException() {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(card1.getId());
        request.setToCardId(card2.getId());
        request.setAmount(new BigDecimal("2000"));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));
        when(cardRepository.findById(card2.getId())).thenReturn(Optional.of(card2));
        when(authUtils.isCardOwnerOrAdmin(user.getId())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(request));
        assertEquals("Недостаточно средств на карте отправителя", ex.getMessage());
    }

    @Test
    void transfer_NotOwner_ThrowsException() {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(card1.getId());
        request.setToCardId(card2.getId());
        request.setAmount(new BigDecimal("100"));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));
        when(cardRepository.findById(card2.getId())).thenReturn(Optional.of(card2));
        when(authUtils.isCardOwnerOrAdmin(user.getId())).thenReturn(false);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> transferService.transfer(request));
        assertEquals("Доступ запрещён: это не ваша карта", ex.getMessage());
    }
}
