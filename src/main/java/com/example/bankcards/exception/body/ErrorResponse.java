package com.example.bankcards.exception.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Представляет структурированный ответ об ошибке, возвращаемый API.
 * Содержит код ошибки, сообщение и временную метку возникновения ошибки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * Код ошибки для категоризации и идентификации типа ошибки.
     * Например: "INTERNAL_SERVER", "VALIDATION_ERROR", "NOT_FOUND" и т.д.
     */
    private String code;

    /**
     * Человекочитаемое сообщение об ошибке, описывающее проблему.
     */
    private String message;

    /**
     * Временная метка, указывающая, когда произошла ошибка.
     * Форматируется в строку по шаблону "yyyy-MM-dd'T'HH:mm:ss".
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}