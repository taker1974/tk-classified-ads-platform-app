package ru.spb.tksoft.ads.mapper;

import java.util.Set;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
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
     * @param comment Comment entity.
     * @return Comment DTO.
     */
    @NotNull
    public static CommentResponseDto toDto(final ResourceService resourceService,
            final CommentEntity comment) {

        UserEntity user = comment.getUser();
        Long userId = user.getId();

        return new CommentResponseDto(comment.getId(),
                userId,
                resourceService.getAvatarImageUrl(userId),
                user.getFirstName(),
                comment.getCreatedAt().toEpochMilli(),
                comment.getText());
    }

    /**
     * Entity to DTO.
     * 
     * @param resultSet Result set.
     * @return Comment DTO.
     */
    @NotNull
    public static CommentsArrayResponseDto toDto(Set<CommentResponseDto> resultSet) {

        return new CommentsArrayResponseDto(resultSet.size(), resultSet);
    }
}
