package ru.spb.tksoft.common.controller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Common error data.
 * 
 * @param code Error code.
 * @param message Error message.
 * @param details Error details.
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public record CommonErrorResponseDto(int code, String message, String details) {

    /**
     * Constructor.
     * 
     * @param code Код ошибки.
     * @param message Описание ошибки.
     */
    public CommonErrorResponseDto(int code, @NotBlank String message) {

        this(code, message, "");
    }
}
