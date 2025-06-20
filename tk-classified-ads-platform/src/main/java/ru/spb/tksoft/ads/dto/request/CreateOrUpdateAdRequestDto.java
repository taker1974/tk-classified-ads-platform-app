package ru.spb.tksoft.ads.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create or update ad request DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "title", "price", "description" })
public class CreateOrUpdateAdRequestDto {

    /** Title. */
    @NotBlank
    @Size(min = 4, max = 32)
    private String title;

    /** Price. */
    @Min(0)
    @Max(10_000_000)
    private int price;

    /** Description. */
    @NotBlank
    @Size(min = 8, max = 64)
    private String description;
}
