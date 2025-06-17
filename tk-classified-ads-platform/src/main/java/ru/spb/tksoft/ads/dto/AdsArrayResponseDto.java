package ru.spb.tksoft.ads.dto;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ads array response DTO.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"count", "items"})
public class AdsArrayResponseDto {

    /** Items count. */
    @Min(value = 0)
    @Max(value = 10_000)
    private int count;

    /** Items array. */
    @NotNull
    private Set<AdResponseDto> items;
}
