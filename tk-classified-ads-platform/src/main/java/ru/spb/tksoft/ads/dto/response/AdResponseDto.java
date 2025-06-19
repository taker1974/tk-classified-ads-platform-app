package ru.spb.tksoft.ads.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    /** User/creator/author ID. */
    @JsonProperty("author")
    private long userId;

    /** Image URL. */
    @JsonProperty("image")
    private String imageUrl;

    /** Price. */
    private int price;

    /** Title. */
    private String title;
}
