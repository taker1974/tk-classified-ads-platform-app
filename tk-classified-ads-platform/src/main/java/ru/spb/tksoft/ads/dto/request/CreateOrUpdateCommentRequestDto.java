package ru.spb.tksoft.ads.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment request DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCommentRequestDto {

    /** Comment text. */
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
