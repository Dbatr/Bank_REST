package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с переводами между своими картами.
 */
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Переводы между своими картами")
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "Перевод между своими картами")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TransferResponse transfer(@RequestBody TransferRequest dto) {
        return transferService.transfer(dto);
    }
}
