package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;

/**
 * Сервис для выполнения переводов средств между картами одного пользователя.
 */
public interface TransferService {
    /**
     * Выполняет перевод средств между картами текущего пользователя.
     * Переводы доступны только между своими картами.
     *
     * @param dto объект запроса {@link TransferRequest}
     * @return объект {@link TransferResponse}
     */
    TransferResponse transfer(TransferRequest dto);
}
