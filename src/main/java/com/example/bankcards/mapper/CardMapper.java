package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Маппер для преобразования между сущностью Card и DTO.
 * Использует MapStruct для генерации кода во время компиляции.
 */
@Mapper(componentModel = "spring")
public interface CardMapper {

    /**
     * Преобразует сущность Card в CardResponse.
     *
     * @param card сущность карты
     * @return DTO ответа с картой
     */
    @Mapping(target = "maskedCardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    @Mapping(target = "userId", source = "owner.id")
    @Mapping(target = "userFullName", source = "owner.fullName")
    CardResponse toCardResponse(Card card);

    /**
     * Маскирует номер карты, оставляя только последние 4 цифры.
     *
     * @param cardNumber исходный номер карты
     * @return маскированный номер карты
     */
    @Named("maskCardNumber")
    default String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}
