package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
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
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;

/**
 * User service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserServiceCached {

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
        clearCache("existsByNameAndPassword");
        clearCache("findUserByName");
        clearCache("getAvatar");
    }

    /**
     * Check if user exists.
     * 
     * @param userName Name.
     * @return True if user exists.
     */
    //@Cacheable(value = "existsByName", key = "#userName")
    public Boolean existsByName(String userName) {

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        return userRepository.existsByName(userName);
    }

    /**
     * Check if user exists.
     * 
     * Password must be encoded befor.
     * 
     * @param userName Name.
     * @param passwordEncoded Password.
     * @return True if user exists.
     */
    //@Cacheable(value = "existsByNameAndPassword", key = "#userName + '_' + #passwordEncoded")
    public Boolean existsByNameAndPassword(String userName, String passwordEncoded) {

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        if (passwordEncoded == null) {
            throw new TkNullArgumentException("password");
        }
        return userRepository.existsByNameAndPassword(userName, passwordEncoded);
    }

    /**
     * Find user by name.
     * 
     * @param userName Name.
     * @return DTO if found, empty DTO otherwise.
     */
    //@Cacheable(value = "findUserByName", key = "#userName")
    public UserResponseDto findUserByName(String userName) {

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }

        UserEntity user = userRepository.findByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        UserResponseDto dto = UserMapper.toDto(user);

        if (user.getAvatar() != null) {
            dto.setImage(resourceService.getAvatarImageUrl(user.getId()));
        }

        return dto;
    }

    /**
     * Get avatar by user id.
     * 
     * @param userId User ID.
     * @return Image resource.
     */
    //@Cacheable(value = "getAvatar", key = "#userId")
    public ResponseEntity<Resource> getAvatar(final long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new TkUserNotFoundException(String.valueOf(userId), false));

        if (user.getAvatar() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = user.getAvatar().getName();
        if (filename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = resourceService.getAvatarImagePath(filename);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new PathResource(filePath);
        MediaType mediaType = MediaType.parseMediaType(user.getAvatar().getMediatype());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
