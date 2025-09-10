package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardActionRequestCreateDto;
import com.example.bankcards.dto.CardActionRequestDto;
import com.example.bankcards.dto.CardActionRequestUpdateDto;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardActionRequestMapper;
import com.example.bankcards.repository.CardActionRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardActionRequestServiceImplTest {

    @Mock
    private CardActionRequestRepository requestRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardActionRequestMapper mapper;

    @Mock
    private AuthUtils authUtils;

    @InjectMocks
    private CardActionRequestServiceImpl service;

    private UUID userId;
    private Card card;
    private CardActionRequest request;
    private CardActionRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();

        User owner = new User();
        owner.setId(userId);

        card = Card.builder()
                .id(UUID.randomUUID())
                .owner(owner)
                .cardNumber("1234567890123456")
                .ownerName("Test User")
                .expiryDate(LocalDate.now().plusYears(1))
                .status(CardStatus.ACTIVE)
                .balance(null)
                .build();

        request = CardActionRequest.builder()
                .id(UUID.randomUUID())
                .card(card)
                .actionType(CardActionType.BLOCK)
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        requestDto = new CardActionRequestDto();
        requestDto.setId(request.getId());
    }

    @Test
    void createRequest_Success() {
        CardActionRequestCreateDto createDto = new CardActionRequestCreateDto();
        createDto.setCardId(card.getId());
        createDto.setActionType(CardActionType.BLOCK);

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        when(requestRepository.save(any(CardActionRequest.class))).thenReturn(request);
        when(mapper.toDto(request)).thenReturn(requestDto);

        CardActionRequestDto result = service.createRequest(createDto, userId);

        assertEquals(request.getId(), result.getId());
        verify(requestRepository, times(1)).save(any(CardActionRequest.class));
    }

    @Test
    void createRequest_CardNotFound() {
        CardActionRequestCreateDto createDto = new CardActionRequestCreateDto();
        createDto.setCardId(UUID.randomUUID());
        createDto.setActionType(CardActionType.BLOCK);

        when(cardRepository.findById(createDto.getCardId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.createRequest(createDto, userId));

        assertTrue(ex.getMessage().contains("Карта не найдена"));
    }

    @Test
    void createRequest_NotOwner() {
        CardActionRequestCreateDto createDto = new CardActionRequestCreateDto();
        createDto.setCardId(card.getId());
        createDto.setActionType(CardActionType.BLOCK);

        UUID otherUserId = UUID.randomUUID();

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> service.createRequest(createDto, otherUserId));

        assertTrue(ex.getMessage().contains("Вы не можете создавать запросы для чужих карт"));
    }

    @Test
    void getAllRequests_ReturnsList() {
        when(requestRepository.findAll()).thenReturn(List.of(request));
        when(mapper.toDto(request)).thenReturn(requestDto);

        List<CardActionRequestDto> result = service.getAllRequests();

        assertEquals(1, result.size());
        assertEquals(request.getId(), result.get(0).getId());
    }

    @Test
    void getRequestById_Success_Admin() {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(authUtils.isAdmin()).thenReturn(true);
        when(mapper.toDto(request)).thenReturn(requestDto);

        CardActionRequestDto result = service.getRequestById(request.getId(), UUID.randomUUID());

        assertEquals(request.getId(), result.getId());
    }

    @Test
    void getRequestById_Success_Owner() {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(authUtils.isAdmin()).thenReturn(false);
        when(mapper.toDto(request)).thenReturn(requestDto);

        CardActionRequestDto result = service.getRequestById(request.getId(), userId);

        assertEquals(request.getId(), result.getId());
    }

    @Test
    void getRequestById_NotOwner() {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(authUtils.isAdmin()).thenReturn(false);

        UUID otherUserId = UUID.randomUUID();
        SecurityException ex = assertThrows(SecurityException.class,
                () -> service.getRequestById(request.getId(), otherUserId));

        assertTrue(ex.getMessage().contains("Доступ запрещен"));
    }

    @Test
    void getRequestById_NotFound() {
        UUID requestId = UUID.randomUUID();
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.getRequestById(requestId, userId));

        assertTrue(ex.getMessage().contains("Запрос не найден"));
    }

    @Test
    void updateRequestStatus_Approved_Block() {
        CardActionRequestUpdateDto updateDto = new CardActionRequestUpdateDto();
        updateDto.setStatus(RequestStatus.APPROVED);

        request.setActionType(CardActionType.BLOCK);

        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);
        when(mapper.toDto(request)).thenReturn(requestDto);

        CardActionRequestDto result = service.updateRequestStatus(request.getId(), updateDto);

        assertEquals(request.getId(), result.getId());
        assertEquals(CardStatus.BLOCKED, request.getCard().getStatus());
        assertNotNull(request.getProcessedAt());
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    void updateRequestStatus_Approved_Activate() {
        CardActionRequestUpdateDto updateDto = new CardActionRequestUpdateDto();
        updateDto.setStatus(RequestStatus.APPROVED);

        request.setActionType(CardActionType.ACTIVATE);
        request.getCard().setStatus(CardStatus.BLOCKED);

        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(requestRepository.save(any(CardActionRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDto(any(CardActionRequest.class))).thenReturn(requestDto);

        CardActionRequestDto result = service.updateRequestStatus(request.getId(), updateDto);

        assertEquals(CardStatus.ACTIVE, request.getCard().getStatus());
    }


    @Test
    void updateRequestStatus_NotFound() {
        CardActionRequestUpdateDto updateDto = new CardActionRequestUpdateDto();
        UUID requestId = UUID.randomUUID();

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.updateRequestStatus(requestId, updateDto));

        assertTrue(ex.getMessage().contains("Запрос не найден"));
    }
}
