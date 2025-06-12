package ru.spb.tksoft.ads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.UserDto;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User service. Cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserServiceCached {

    private final Logger log = LoggerFactory.getLogger(UserServiceCached.class);

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final CacheManager cacheManager;

    private void clearCache(String name) {

        Cache cache = cacheManager.getCache(name);
        if (cache == null) {
            throw new IllegalStateException("Cache " + name + " not found");
        }
        cache.clear();
    }

    /** Clear caches. */
    public void clearCaches() {

        clearCache("user");
    }

    /**
     * Find user by name and password.
     * 
     * @return DTO.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()", key = "#name")
    @NotNull
    public UserDto findUserByNameAndPassword(@NotBlank final String email,
            @NotBlank final String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity entity = userRepository.findOneByNameAndPassword(email, password)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given credentials not found"));

        UserDto dto = UserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }
}
