package ru.spb.tksoft.ads.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"pk", "author", "authorImage", "authorFirstName", "createdAt", "text"})
public class CommentResponseDto {

    /** Comment ID. */
    @JsonProperty("pk")
    private long id;

    /** Author ID. */
    @JsonProperty("author")
    private long authorId;

    /** Author's image URL. */
    @JsonProperty("authorImage")
    private String authorAvatarUrl;

    /** Author's first name. */
    private String authorFirstName;

    /** Created at timestamp. Milliseconds since Unix epoch (00:00:00 01.01.1970). */
    private long createdAt;

    /** Comment text. */
    private String text;
}
