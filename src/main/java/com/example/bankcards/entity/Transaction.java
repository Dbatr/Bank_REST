package com.example.bankcards.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность транзакции между картами.
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    /**
     * Уникальный идентификатор транзакции.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID карты отправителя (ссылка на {@link Card}).
     */
    @Column(name = "from_card_id", nullable = false)
    private UUID fromCardId;

    /**
     * ID карты получателя (ссылка на {@link Card}).
     */
    @Column(name = "to_card_id", nullable = false)
    private UUID toCardId;

    /**
     * Сумма перевода.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Дата и время создания транзакции.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * ID пользователя, выполнившего транзакцию (ссылка на {@link User}).
     */
    @Column(name = "performed_by_user_id", nullable = false)
    private UUID performedByUserId;
}

