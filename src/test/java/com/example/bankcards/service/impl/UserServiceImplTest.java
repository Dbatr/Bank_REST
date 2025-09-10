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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private Role role;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setName("User");

        user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setFullName("Test User");
        user.setRoles(Set.of(role));

        userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername("testuser");
        userResponse.setFullName("Test User");
        userResponse.setRoles(Set.of("USER"));
    }

    @Test
    void getAllUsers_ReturnsPage() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.getAllUsers(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals(userId, result.getContent().get(0).getId());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getUserById_Found() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getUserById(userId));

        assertTrue(ex.getMessage().contains("Пользователь не найден"));
    }

    @Test
    void createUser_Success() {
        UserRequest request = new UserRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setRoles(Set.of("USER"));

        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findById("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void updateUser_Success() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFullName("Updated Name");
        request.setRoles(Set.of("USER"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(userId, request);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).save(user);
        assertEquals("Updated Name", user.getFullName());
    }

    @Test
    void updateUser_NotFound() {
        UserUpdateRequest request = new UserUpdateRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.updateUser(userId, request));

        assertTrue(ex.getMessage().contains("Пользователь не найден"));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.deleteUser(userId));

        assertTrue(ex.getMessage().contains("Пользователь не найден"));
    }
}
