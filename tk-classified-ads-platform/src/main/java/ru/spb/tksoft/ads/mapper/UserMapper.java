package ru.spb.tksoft.ads.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.UserDto;
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
     * @param userEntity User entity.
     * @return user DTO.
     */
    @NotNull
    public static UserDto toDto(@NotNull final UserEntity entity) {

        return new UserDto(
                entity.getId(),
                entity.getName(), // as email/login
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getRole(),
                "link-to-avatar");
    }
}
