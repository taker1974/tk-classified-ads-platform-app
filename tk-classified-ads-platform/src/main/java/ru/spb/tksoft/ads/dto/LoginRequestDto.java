package ru.spb.tksoft.ads.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request, basic authentication.
 * 
 * No response DTO. Http response: 201 (Created) or 400 (Bad Request).
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /** Login aka username. */
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    /** Password. */
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
