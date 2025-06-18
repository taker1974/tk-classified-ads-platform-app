package ru.spb.tksoft.ads.dto.response;

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
    private String email;

    /** First name. */
    private String firstName;

    /** Last name. */
    private String lastName;

    /** Phone number. */
    private String phone;

    /** User's role. */
    private UserRole role;

    /** Link to the user's avatar. */
    private String image;
}
