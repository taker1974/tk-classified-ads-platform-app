package ru.spb.tksoft.ads.mapper;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.service.ResourceService;

/**
 * Mapper for Ad*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class AdMapper {

    private AdMapper() {}

    /**
     * Gets first image file name.
     * 
     * @param entity Ad entity.
     * @return File name.
     */
    public static String getFirstImageFileName(final AdEntity entity) {

        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            return entity.getImages().getFirst().getName();
        }
        return "";
    }

    /**
     * Entity to DTO.
     * 
     * @param entity Ad entity.
     * @return user DTO.
     */
    @SuppressWarnings("java:S1066") // Merge this if statement with the enclosing one.
    @NotNull
    public static AdResponseDto toDto(final ResourceService resourceService,
            final AdEntity entity) {

        String image = "";
        List<ImageEntity> images = entity.getImages();
        if (images != null) {
            if (!images.isEmpty()) {
                image = resourceService.getAdImageUrl(entity.getImages().getFirst().getId());
            }
        }

        return new AdResponseDto(entity.getId(),
                entity.getUser().getId(),
                image,
                entity.getPrice().intValue(),
                entity.getTitle());
    }

    /**
     * DTO to entity.
     * 
     * @param dto DTO.
     * @return New entity.
     */
    @NotNull
    public static AdEntity toEntity(final CreateOrUpdateAdRequestDto dto) {

        return new AdEntity(
                dto.getTitle(),
                BigDecimal.valueOf(dto.getPrice()),
                dto.getDescription());
    }
}
