package ru.spb.tksoft.ads.mapper;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.projection.AdExtendedResponseProjection;
import ru.spb.tksoft.ads.projection.AdResponseProjection;
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
     * AdResponseProjection to DTO.
     * 
     * @param resourceService Resource service.
     * @param projection AdResponseProjection.
     * @return Response DTO.
     */
    @NotNull
    public static AdResponseDto toDto(final ResourceService resourceService,
            final AdResponseProjection projection) {

        return new AdResponseDto(projection.getId(),
                projection.getUserId(),
                resourceService.getAdImageUrl(projection.getImageId()),
                projection.getPrice().intValue(),
                projection.getTitle());
    }

    /**
     * Set of AdResponseDto to data DTO.
     * 
     * @param size Amount of items.
     * @param responseSet Items.
     * @return Response set.
     */
    @NotNull
    public static AdsArrayResponseDto toAdsDto(int size,
            final Set<AdResponseDto> responseSet) {

        return new AdsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * AdEntity to DTO.
     * 
     * @param resourceService Resource service.
     * @param entity Ad entity.
     * @return Response DTO.
     */
    @NotNull
    public static AdResponseDto toDto(final ResourceService resourceService,
            final AdEntity entity) {

        return new AdResponseDto(entity.getId(),
                entity.getUser().getId(),
                resourceService.getAdImageUrl(entity.getId()),
                entity.getPrice().intValue(),
                entity.getTitle());
    }

    /**
     * CreateOrUpdateAdRequestDto to entity.
     *
     * @param dto Request DTO.
     * @return New ad entity.
     */
    @NotNull
    public static AdEntity toEntity(final CreateOrUpdateAdRequestDto dto) {

        return new AdEntity(
                dto.getTitle(),
                BigDecimal.valueOf(dto.getPrice()),
                dto.getDescription());
    }

    /**
     * Ad projection to extended DTO.
     *
     * @param projection Ad projection.
     * @return Extended ad DTO.
     */
    @NotNull
    public static AdExtendedResponseDto toDto(final ResourceService resourceService,
            final AdExtendedResponseProjection projection) {

        return new AdExtendedResponseDto(
                projection.getId(),
                projection.getTitle(), projection.getPrice().intValue(),
                projection.getDescription(),
                resourceService.getAdImageUrl(projection.getImageId()),
                projection.getAuthorFirstName(), projection.getAuthorLastName(),
                projection.getEmail(), projection.getPhone());
    }
}
