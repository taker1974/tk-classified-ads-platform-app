package ru.spb.tksoft.ads.mapper;

import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.service.ResourceService;

/**
 * Mapper for User*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class UserMapper {

    private UserMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity User entity.
     * @return user DTO.
     */
    @NotNull
    public static UserResponseDto toDto(final ResourceService resourceService,
            final UserEntity entity) {

        return new UserResponseDto(
                entity.getId(),
                entity.getName(), // as email/login
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getRole(),
                resourceService.getAvatarImageUrl(entity.getId()));
    }

    /**
     * Entity to DTO.
     * 
     * @param updateRequest Update request DTO.
     * @return Response DTO.
     */
    @NotNull
    public static UpdateUserResponseDto toDto(final UpdateUserRequestDto updateRequest) {

        return new UpdateUserResponseDto(
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                updateRequest.getPhone());
    }

    /**
     * DTO to entity.
     * 
     * @param dto New user DTO.
     * @return User entity.
     */
    public static UserEntity toEntity(final RegisterRequestDto dto) {

        return new UserEntity(dto.getUsername(), dto.getPassword(),
                dto.getFirstName(), dto.getLastName(),
                dto.getPhone(),
                dto.getRole());
    }
}
