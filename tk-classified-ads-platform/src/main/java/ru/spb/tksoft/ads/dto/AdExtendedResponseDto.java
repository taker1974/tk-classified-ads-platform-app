package ru.spb.tksoft.ads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 2, max = 16)
    private String authorFirstName;

    /** Author's last name. */
    @NotBlank
    @Size(min = 2, max = 16)
    private String authorLastName;

    /** Description. */
    @NotBlank
    @Size(min = 8, max = 64)
    private String description;

    /** Author's login name / email. */
    @NotBlank
    @Email
    @Size(min = 4, max = 32)
    private String email;

    /** Image URL. */
    @NotBlank
    @Size(min = 10, max = 512)
    @JsonProperty("image")
    private String imageUrl;

    /** Author's phone number. */
    @NotBlank
    @Size(min = 11, max = 32)
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    /** Price. */
    @Min(0)
    @Max(10_000_000)
    private int price;

    /** Title. */
    @NotBlank
    @Size(min = 4, max = 32)
    private String title;
}
