package ru.spb.tksoft.ads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ad response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"pk", "author", "image", "price", "title"})
public class AdResponseDto {

    /** Ad ID. */
    @JsonProperty("pk")
    private long id;

    /** Author ID. */
    @JsonProperty("author")
    private long authorId;

    /** Image URL. */
    @NotBlank
    @Size(min = 10, max = 512)
    @JsonProperty("image")
    private String imageUrl;

    /** Price. */
    @Min(0)
    @Max(10_000_000)
    private int price;

    /** Title. */
    @NotBlank
    @Size(min = 4, max = 32)
    private String title;
}
