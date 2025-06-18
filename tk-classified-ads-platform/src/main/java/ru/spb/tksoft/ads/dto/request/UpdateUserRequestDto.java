package ru.spb.tksoft.ads.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update user response/request DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    /** First name. */
    @NotBlank
    @Size(min = 2, max = 16)
    private String firstName;

    /** Last name. */
    @NotBlank
    @Size(min = 2, max = 16)
    private String lastName;

    /** Phone number. */
    @NotBlank
    @Size(min = 11, max = 32)
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
