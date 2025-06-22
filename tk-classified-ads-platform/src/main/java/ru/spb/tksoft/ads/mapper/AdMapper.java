package ru.spb.tksoft.ads.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.service.ResourceService;

/**
 * Mapper for Ad*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ThreadSafe
public final class AdMapper {

    private AdMapper() {}

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
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            image = resourceService.getAvatarImageUrl(entity.getImages().getFirst().getId());
        }

        return new AdResponseDto(entity.getId(), 
            entity.getUser().getId(), 
            image, 
            entity.getPrice().intValue(), 
            entity.getTitle());
    }
}
