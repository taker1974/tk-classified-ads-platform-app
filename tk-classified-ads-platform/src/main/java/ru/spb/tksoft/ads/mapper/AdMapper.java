package ru.spb.tksoft.ads.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
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
    @NotNull
    public static AdResponseDto toDto(final ResourceService resourceService,
            final AdEntity entity) {

        String image = "";
        List<ImageEntity> images = entity.getImages();
        if (images != null && !images.isEmpty()) {
            image = resourceService.getAdImageUrl(entity.getImages().getFirst().getId());
        }

        return new AdResponseDto(entity.getId(),
                entity.getUser().getId(),
                image,
                entity.getPrice().intValue(),
                entity.getTitle());
    }

    /**
     * Entity to extended DTO.
     * 
     * @param e Ad entity.
     * @return user DTO.
     */
    @NotNull
    public static AdExtendedResponseDto toExtendedDto(final ResourceService resourceService,
            final AdEntity e) {

        String image = "";
        List<ImageEntity> images = e.getImages();
        if (images != null && !images.isEmpty()) {
            image = resourceService.getAdImageUrl(e.getImages().getFirst().getId());
        }

        UserEntity u = e.getUser();
        return new AdExtendedResponseDto(
                e.getId(), e.getTitle(), e.getPrice().intValue(), e.getDescription(),
                image,
                u.getFirstName(), u.getLastName(), u.getName(), u.getPhone());
    }

    /**
     * Comments entity to DTO.
     * 
     * @param size Amount of comments.
     * @param responseSet Comments set.
     * @return Response array DTO.
     */
    @NotNull
    public static CommentsArrayResponseDto toCommentsDto(int size,
            final Set<CommentResponseDto> responseSet) {

        return new CommentsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * Owned ads entity to DTO.
     * 
     * @param size Amount of comments.
     * @param responseSet Comments set.
     * @return Response array DTO.
     */
    @NotNull
    public static AdsArrayResponseDto toAdsDto(int size,
            final Set<AdResponseDto> responseSet) {

        return new AdsArrayResponseDto(responseSet.size(), responseSet);
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
