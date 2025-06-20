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
@JsonPropertyOrder({"pk", "title", "description", "price", "image", "authorFirstName", "authorLastName", "email", "phone"})
public class AdExtendedResponseDto {

    /** Ad ID. */
    @JsonProperty("pk")
    private long id;

    /** Author's first name. */
    private String authorFirstName;

    /** Author's last name. */
    private String authorLastName;

    /** Description. */
    private String description;

    /** Author's login name / email. */
    private String email;

    /** Image URL. */
    @JsonProperty("image")
    private String imageUrl;

    /** Author's phone number. */
    private String phone;

    /** Price. */
    private int price;

    /** Title. */
    private String title;
}
