package ru.spb.tksoft.ads.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ad extended response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"pk", "authorFirstName", "authorLastName", "description", "email", "image",
        "phone", "price", "title"})
public class AdExtendedResponseDto {

    /** Ad ID. */
    @JsonProperty("pk")
    private long id;

    /** Title. */
    private String title;

    /** Price. */
    private int price;

    /** Description. */
    private String description;

    /** Image URL. */
    private String image;

    /** Author's first name. */
    private String authorFirstName;

    /** Author's last name. */
    private String authorLastName;

    /** Author's login name / email. */
    private String email;

    /** Author's phone number. */
    private String phone;
}
