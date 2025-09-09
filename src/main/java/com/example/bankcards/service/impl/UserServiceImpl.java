package com.example.bankcards.service.impl;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.dto.UserUpdateRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findById(roleName)
                        .orElseThrow(() -> new NotFoundException("Роль не найдена: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getRoles() != null) {
            Set<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findById(roleName)
                            .orElseThrow(() -> new NotFoundException("Роль не найдена: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь не найден: " + id);
        }
        userRepository.deleteById(id);
    }
}
