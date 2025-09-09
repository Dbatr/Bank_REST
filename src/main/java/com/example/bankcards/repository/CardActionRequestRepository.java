package com.example.bankcards.repository;

import com.example.bankcards.entity.CardActionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью CardActionRequest.
 */
@Repository
public interface CardActionRequestRepository extends JpaRepository<CardActionRequest, UUID> {
}