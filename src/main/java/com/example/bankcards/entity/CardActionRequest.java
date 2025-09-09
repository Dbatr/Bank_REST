package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность запроса на блокировку или активацию карты.
 * Создается пользователем и обрабатывается админом.
 */
@Entity
@Table(name = "card_action_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardActionRequest {

    /**
     * Уникальный идентификатор запроса (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Карта, для которой создается запрос.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    /**
     * Тип действия: блокировка или активация.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private CardActionType actionType;

    /**
     * Статус запроса: в ожидании, одобрен или отклонен.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Дата и время обработки запроса админом.
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
