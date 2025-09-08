package com.example.bankcards.exception;

import com.example.bankcards.exception.body.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для приложения.
 * Перехватывает исключения, возникающие в контроллерах, и возвращает структурированные ответы об ошибках.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает все не перехваченные исключения типа Exception и его подклассов.
     *
     * @param ex исключение, которое было выброшено
     * @return ResponseEntity с объектом ErrorResponse и статусом HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        var error = new ErrorResponse("INTERNAL_SERVER", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Обрабатывает исключения аутентификации.
     * Вызывается при неверных учетных данных пользователя.
     *
     * @return ResponseEntity с объектом ErrorResponse и статусом HTTP 401 (Unauthorized)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException() {
        var error = new ErrorResponse("UNAUTHORIZED", "Неверные учетные данные пользователя", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Обрабатывает исключения безопасности.
     * Вызывается когда пользователь не имеет прав доступа к ресурсу.
     *
     * @param ex исключение SecurityException
     * @return ResponseEntity с объектом ErrorResponse и статусом HTTP 403 (Forbidden)
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
        var error = new ErrorResponse("FORBIDDEN", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}