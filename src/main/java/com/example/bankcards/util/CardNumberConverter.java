package com.example.bankcards.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Конвертер для шифрования и дешифрования номеров банковских карт
 * при сохранении в БД и извлечении из неё.
 * Используется AES в режиме GCM (без паддинга).
 */
@Component
@Converter(autoApply = true)
public class CardNumberConverter implements AttributeConverter<String, String> {

    private static final String ALG = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_BIT_LENGTH = 128;

    private static SecretKeySpec key;

    @Value("${app.crypto.key}")
    private String base64Key;

    /**
     * Инициализация ключа после внедрения значения через Spring.
     */
    @PostConstruct
    private void init() {
        if (base64Key == null) {
            throw new IllegalStateException("Crypto key is not set");
        }
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        key = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Шифрует значение перед сохранением в БД.
     * @param attribute открытый номер карты
     * @return зашифрованная строка (Base64)
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALG);
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] cipherText = cipher.doFinal(attribute.getBytes());
            ByteBuffer bb = ByteBuffer.allocate(iv.length + cipherText.length);
            bb.put(iv);
            bb.put(cipherText);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) {
            throw new RuntimeException("Cannot encrypt card number", e);
        }
    }

    /**
     * Дешифрует значение после получения из БД.
     * @param dbData зашифрованная строка (Base64)
     * @return расшифрованный номер карты
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] all = Base64.getDecoder().decode(dbData);
            ByteBuffer bb = ByteBuffer.wrap(all);
            byte[] iv = new byte[IV_SIZE];
            bb.get(iv);
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);
            Cipher cipher = Cipher.getInstance(ALG);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain);
        } catch (Exception e) {
            throw new RuntimeException("Cannot decrypt card number", e);
        }
    }
}
