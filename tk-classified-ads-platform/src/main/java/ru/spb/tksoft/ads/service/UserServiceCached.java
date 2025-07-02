package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.entity.AvatarEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserServiceCached {

    private final Logger log = LoggerFactory.getLogger(UserServiceCached.class);

    private final UserRepository userRepository;
    private final ResourceService resourceService;

    private final CacheManager cacheManager;

    private void clearCache(String name) {

        Cache cache = cacheManager.getCache(name);
        assert (cache != null);

        cache.clear();
    }

    /** Clear caches. */
    public void clearCaches() {

        clearCache("existsByName");
        clearCache("getUserEntityLazy");
        clearCache("getUser");
        clearCache("getAvatar");
    }

    /**
     * Check if user exists. Used in registration procedure. Used.
     * 
     * @param userName Name.
     * @return True if user exists.
     */
    @Cacheable(value = "existsByName", key = "#userName")
    public Boolean existsByName(String userName) {

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        return userRepository.existsByName(userName);
    }

    /**
     * Get UserEntity by name lazily.
     * 
     * @param userName Name.
     * @return UserEntity.
     * @throws TkNullArgumentException If userName is null.
     * @throws TkUserNotFoundException If user not found.
     */
    @Cacheable(value = "getUserEntityLazy", key = "#userName")
    public UserEntity getUserEntityLazy(final String userName) {

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        return userRepository.findOneByNameLazy(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));
    }

    /**
     * Get UserResponseDto by name.
     * 
     * @param userName Name.
     * @return Response DTO.
     */
    @Cacheable(value = "getUser", key = "#userName")
    public UserResponseDto getUser(final String userName) {

        if (userName == null || userName.isBlank()) {
            throw new TkNullArgumentException("userName");
        }
        UserEntity user = userRepository.findOneByNameEager(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        return UserMapper.toDto(resourceService, user);
    }

    /**
     * Get avatar by user ID.
     * 
     * @param userId User ID.
     * @return Image resource.
     */
    @Cacheable(value = "getAvatar", key = "#userId")
    public ResponseEntity<Resource> getAvatar(final Long userId) {

        UserEntity user = userRepository.findOneByIdEager(userId)
                .orElseThrow(() -> new TkUserNotFoundException(
                        userId.toString(), false));

        AvatarEntity avatar = user.getAvatar();
        if (avatar == null) {
            return resourceService.getDefaultAvatar();
        }

        String filename = avatar.getName();
        if (filename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = resourceService.getAvatarImagePath(filename);
        if (!Files.exists(filePath)) {
            LogEx.error(log, LogEx.getThisMethodName(),
                    "User " + userId + ": " + "avatar file \"" + filename + "\" not found");
            return resourceService.getDefaultAvatar();
        }

        Resource resource = new PathResource(filePath);
        MediaType mediaType = MediaType.parseMediaType(avatar.getMediatype());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
