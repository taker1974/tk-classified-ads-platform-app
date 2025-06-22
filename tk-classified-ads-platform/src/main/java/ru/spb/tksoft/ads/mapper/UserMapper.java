package ru.spb.tksoft.ads.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.entity.UserEntity;

/**
 * Mapper for User*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ThreadSafe
public final class UserMapper {

    private UserMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity User entity.
     * @return user DTO.
     */
    @NotNull
    public static UserResponseDto toDto(final UserEntity entity) {

        return new UserResponseDto(
                entity.getId(),
                entity.getName(), // as email/login
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getRole(),
                entity.getAvatar() == null ? "no avatar" : entity.getAvatar().getName());
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
