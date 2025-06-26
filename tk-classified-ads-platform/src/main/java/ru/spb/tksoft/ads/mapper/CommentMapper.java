package ru.spb.tksoft.ads.mapper;

import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.service.ResourceService;

/**
 * Mapper for Comment*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class CommentMapper {

    private CommentMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity Comment entity.
     * @return Comment DTO.
     */
    @NotNull
    public static CommentResponseDto toDto(final ResourceService resourceService,
            final CommentEntity entity) {

        UserEntity user = entity.getUser();
        String avatar = "";
        if (user.getAvatar() != null) {
            avatar = resourceService.getAvatarImageUrl(user.getId());
        }

        return new CommentResponseDto(entity.getId(),
                user.getId(),
                avatar,
                user.getFirstName(),
                entity.getCreatedAt().toEpochMilli(),
                entity.getText());
    }
}
