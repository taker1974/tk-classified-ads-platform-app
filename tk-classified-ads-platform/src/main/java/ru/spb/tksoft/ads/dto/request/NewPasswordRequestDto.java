package ru.spb.tksoft.ads.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Change password request, basic authentication.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequestDto {

    /** Current password. NOT ENCRYPTED. */
    @NotBlank
    @Size(min = 8, max = 16)
    private String currentPassword;

    /** New password. NOT ENCRYPTED. */
    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;
}
