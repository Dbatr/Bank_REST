package com.example.bankcards.entity;

/**
 * Статус банковской карты.
 */
public enum CardStatus {
    /** Карта активна и доступна для операций */
    ACTIVE,
    /** Карта заблокирована */
    BLOCKED,
    /** Срок действия карты истёк */
    EXPIRED
}
