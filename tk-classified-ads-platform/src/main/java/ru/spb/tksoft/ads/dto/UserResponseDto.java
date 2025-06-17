package ru.spb.tksoft.ads.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.tksoft.ads.enumeration.UserRole;

/**
 * User response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    /** User ID. */
    private long id;

    /** Login as username as email. */
    @NotBlank
    @Email
    @Size(min = 4, max = 32)
    private String email;

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

    /** User's role. */
    @NotNull
    private UserRole role;

    /** Link to the user's avatar. */
    @NotBlank
    @Size(min = 10, max = 512)
    private String image;
}
