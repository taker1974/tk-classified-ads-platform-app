package ru.spb.tksoft.ads.dto.response;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comments array response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"count", "items"})
public class CommentsArrayResponseDto {

    /** Items count. */
    private int count;

    /** Items array. */
    private Set<CommentResponseDto> items;
}
