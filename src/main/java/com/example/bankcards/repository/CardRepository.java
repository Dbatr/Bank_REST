package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Card.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
  Page<Card> findByOwnerId(UUID ownerId, Pageable pageable);
  @Query("SELECT c FROM Card c JOIN FETCH c.owner")
  List<Card> findAllWithOwner();
  Page<Card> findAll(Pageable pageable);
}