package com.example.bankcards.entity;

import com.example.bankcards.util.CardNumberConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Сущность, представляющая банковскую карту.
 */
@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    /**
     * Уникальный идентификатор карты (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Номер карты (шифруется при сохранении в БД).
     */
    @Convert(converter = CardNumberConverter.class)
    @Column(name = "card_number", nullable = false, unique = true, length = 2048)
    private String cardNumber;

    /**
     * Имя владельца карты.
     */
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    /**
     * Дата окончания действия карты.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    /**
     * Статус карты (активна, заблокирована, истек срок действия).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    /**
     * Баланс карты.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    /**
     * Владелец карты (ссылка на пользователя).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
}
