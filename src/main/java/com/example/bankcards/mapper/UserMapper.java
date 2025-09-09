package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между сущностью User и DTO.
 * Использует MapStruct для генерации кода во время компиляции.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRolesToNames(user.getRoles()))")
    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRequest request);

    default Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
