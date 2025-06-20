package ru.spb.tksoft.ads.dto.response;

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
public class UpdateUserResponseDto {

    /** First name. */
    private String firstName;

    /** Last name. */
    private String lastName;

    /** Phone number. */
    private String phone;
}
