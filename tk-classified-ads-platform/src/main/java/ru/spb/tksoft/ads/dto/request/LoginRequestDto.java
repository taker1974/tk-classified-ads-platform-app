package ru.spb.tksoft.ads.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request, basic authentication.
 * 
 * No response DTO. Http response:<br>
 * "201": description: Created <br>
 * "401": description: Unauthorized <br>
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /** Login aka username. */
    @NotBlank
    @Email
    @Size(min = 4, max = 32)
    private String username;

    /** Password. Password can/will be encoded in bcrypt so it's length can be larger than 16. */
    @NotBlank
    @Size(min = 8, max = 64)
    private String password;
}
