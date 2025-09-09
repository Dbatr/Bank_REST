package com.example.bankcards.controller;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.dto.UserUpdateRequest;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Контроллер для управления пользователями.
 * Предоставляет эндпоинты для CRUD операций с пользователями.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями (только для ADMIN)")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public Page<UserResponse> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Создать нового пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
