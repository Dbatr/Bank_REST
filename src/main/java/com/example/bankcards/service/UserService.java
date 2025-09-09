package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сервис для управления пользователями системы.
 * Определяет основные операции CRUD над сущностью {@link com.example.bankcards.entity.User}.
 */
public interface UserService {

    /**
     * Получить список всех пользователей с постраничной разбивкой.
     *
     * @param pageable параметры пагинации (номер страницы, размер, сортировка)
     * @return страница с данными пользователей
     */
    Page<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param id уникальный идентификатор пользователя (UUID)
     * @return DTO с данными пользователя
     * @throws com.example.bankcards.exception.NotFoundException если пользователь не найден
     */
    UserResponse getUserById(UUID id);

    /**
     * Создать нового пользователя.
     *
     * @param request DTO с данными для создания (логин, пароль, ФИО, роли)
     * @return DTO созданного пользователя
     * @throws com.example.bankcards.exception.NotFoundException если одна из указанных ролей не существует
     */
    UserResponse createUser(UserRequest request);

    /**
     * Обновить данные существующего пользователя.
     *
     * @param id      уникальный идентификатор пользователя (UUID)
     * @param request DTO с изменяемыми данными (ФИО, роли)
     * @return DTO обновлённого пользователя
     * @throws com.example.bankcards.exception.NotFoundException если пользователь или указанная роль не найдены
     */
    UserResponse updateUser(UUID id, UserUpdateRequest request);

    /**
     * Удалить пользователя по его идентификатору.
     *
     * @param id уникальный идентификатор пользователя (UUID)
     * @throws com.example.bankcards.exception.NotFoundException если пользователь не найден
     */
    void deleteUser(UUID id);
}
