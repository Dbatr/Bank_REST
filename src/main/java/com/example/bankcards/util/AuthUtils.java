package com.example.bankcards.util;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    /**
     * Получает текущего аутентифицированного пользователя
     * @return сущность User текущего пользователя
     * @throws NotFoundException если пользователь не найден
     */
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + username));
    }

    /**
     * Получает ID текущего аутентифицированного пользователя
     * @return UUID пользователя
     * @throws NotFoundException если пользователь не найден
     */
    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Получает username текущего аутентифицированного пользователя
     * @return username пользователя
     * @throws IllegalStateException если пользователь не аутентифицирован
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            throw new IllegalStateException("Неизвестный тип principal: " + principal.getClass());
        }
    }

    /**
     * Проверяет, является ли текущий пользователь администратором
     * @return true если пользователь администратор
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем карты
     * @param cardUserId ID пользователя-владельца карты
     * @return true если пользователь владелец карты или администратор
     */
    public boolean isCardOwnerOrAdmin(UUID cardUserId) {
        if (isAdmin()) {
            return true;
        }

        try {
            UUID currentUserId = getCurrentUserId();
            return currentUserId.equals(cardUserId);
        } catch (Exception e) {
            return false;
        }
    }
}
