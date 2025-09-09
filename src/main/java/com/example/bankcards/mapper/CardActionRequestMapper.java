package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardActionRequestDto;
import com.example.bankcards.entity.CardActionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования между сущностью CardActionRequest и DTO.
 * Использует MapStruct для генерации кода во время компиляции.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardActionRequestMapper {

    /**
     * Преобразует сущность CardActionRequest в DTO.
     *
     * @param entity сущность запроса на действие с картой
     * @return DTO запроса
     */
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardOwnerId", source = "card.owner.id")
    CardActionRequestDto toDto(CardActionRequest entity);
}
