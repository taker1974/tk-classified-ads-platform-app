package ru.spb.tksoft.ads.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Get/transform resource address.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    @Value("${media.avatar-url-base-path}")
    private String avatarUrlBasePath;

    @Value("${media.avatar-storage-path}")
    private String avatarStoragePath;

    @Value("${media.image-url-base-path}")
    private String imageUrlBasePath;

    @Value("${media.image-storage-path}")
    private String imageStoragePath;

    /**
     * Get avatar URL.
     * 
     * @param userId User ID.
     * @return Avatar URL in a form of "/<avatar-base-path>/<user-id>".
     */
    public String getAvatarUrl(long userId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        return Paths.get(avatarUrlBasePath)
                .resolve(String.valueOf(userId)).toString();
    }

    /**
     * Get ad's image URL.
     * 
     * @param imageId Image ID.
     * @return Image URL in a form of "/<image-base-path>/<image-id>".
     */
    public String getImageUrl(long imageId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        return Paths.get(imageUrlBasePath)
                .resolve(String.valueOf(imageId)).toString();
    }

    /**
     * Get avatar path.
     * 
     * @param avatarFileName Avatar file name.
     * @return Full avatar path.
     */
    public Path getAvatarPath(String avatarFileName) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        return Path.of(avatarStoragePath, avatarFileName);
    }

    /**
     * Get image path.
     * 
     * @param imageFileName Image file name.
     * @return Full image path.
     */
    public Path getImagePath(String imageFileName) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        return Path.of(imageStoragePath, imageFileName);
    }
}
