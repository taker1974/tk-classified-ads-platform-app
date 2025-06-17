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
    @NotBlank
    @Size(min = 10, max = 512)
    @JsonProperty("authorImage")
    private String authorAvatarUrl;

    /** Author's first name. */
    @NotBlank
    @Size(min = 2, max = 16)
    private String authorFirstName;

    /** Created at timestamp. Milliseconds since Unix epoch (00:00:00 01.01.1970). */
    @Min(0)
    @Max(4_102_444_800_000L) // 1970-01-01T00:00:00.000Z ~00:00:00 01.01.2099.
    private long createdAt;

    /** Comment text. */
    @NotBlank
    @Size(min = 1, max = 2048)
    private String text;
}
