package com.example.bankcards.service.impl;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Реализация сервиса для переводов между картами.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final AuthUtils authUtils;

    @Override
    public TransferResponse transfer(TransferRequest dto) {
        if (dto.getFromCardId().equals(dto.getToCardId())) {
            throw new IllegalArgumentException("Нельзя перевести на ту же самую карту");
        }

        Card fromCard = cardRepository.findById(dto.getFromCardId())
                .orElseThrow(() -> new NotFoundException("Карта отправителя не найдена"));
        Card toCard = cardRepository.findById(dto.getToCardId())
                .orElseThrow(() -> new NotFoundException("Карта получателя не найдена"));

        if (!fromCard.getOwner().getId().equals(toCard.getOwner().getId())) {
            throw new SecurityException("Перевод возможен только между своими картами");
        }

        if (!authUtils.isCardOwnerOrAdmin(fromCard.getOwner().getId())) {
            throw new SecurityException("Доступ запрещён: это не ваша карта");
        }

        BigDecimal amount = dto.getAmount();
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте отправителя");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        TransferResponse response = new TransferResponse();
        response.setFromCardId(fromCard.getId());
        response.setToCardId(toCard.getId());
        response.setAmount(amount);
        response.setFromCardBalance(fromCard.getBalance());
        response.setToCardBalance(toCard.getBalance());

        return response;
    }
}
