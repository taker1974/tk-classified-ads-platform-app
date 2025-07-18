package ru.spb.tksoft.ads.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.config.ImageProcessingProperties;
import ru.spb.tksoft.ads.exception.TkDeletingMediaException;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkSavingMediaException;
import ru.spb.tksoft.ads.exception.TkSizeException;
import ru.spb.tksoft.ads.exception.TkUnsupportedMediaTypeException;
import ru.spb.tksoft.utils.log.LogEx;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Deal with [file] resource.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    /** Maximum length of content-type string. */
    public static final int CONTENT_TYPE_LENGTH_MAX = 128;

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ImageProcessingProperties avatarImageProcessing;
    private final ImageProcessingProperties adImageProcessing;

    private final ResourceLoader resourceLoader;

    private ResponseEntity<Resource> getDefault(final ImageProcessingProperties properties) {

        try {
            Resource resource = resourceLoader.getResource(properties.fileNameDefault());
            MediaType mediaType = MediaType.parseMediaType(properties.mimeTypeDefault());
            return ResponseEntity.ok().contentType(mediaType).body(resource);
        } catch (Exception ex) {
            LogEx.error(log, LogEx.getThisMethodName(), ex);
            throw ex;
        }
    }

    /**
     * Get default avatar response.
     * 
     * @return Response entity.
     */
    public ResponseEntity<Resource> getDefaultAvatar() {

        return getDefault(avatarImageProcessing);
    }

    /**
     * Get default image response.
     * 
     * @return Response entity.
     */
    public ResponseEntity<Resource> getDefaultAdImage() {

        return getDefault(adImageProcessing);
    }

    /**
     * Get avatar URL.
     * 
     * @param userId User ID.
     * @return Avatar URL in a form of "/avatar-base-path/user-id".
     */
    public String getAvatarImageUrl(long userId) {

        return Paths.get(avatarImageProcessing.urlBasePath())
                .resolve(String.valueOf(userId)).toString();
    }

    /**
     * Get ad's image URL.
     * 
     * @param imageId Image ID.
     * @return Image URL in a form of "/image-base-path/image-id".
     */
    public String getAdImageUrl(long imageId) {

        return Paths.get(adImageProcessing.urlBasePath())
                .resolve(String.valueOf(imageId)).toString();
    }

    /**
     * Get directory with avatars.
     * 
     * @return Path to directory.
     */
    public Path getAvatarsDirectory() {

        return Path.of(avatarImageProcessing.storagePath());
    }

    /**
     * Get avatar path.
     * 
     * @param avatarFileName Avatar file name.
     * @return Full avatar path.
     */
    public Path getAvatarImagePath(String avatarFileName) {

        return Path.of(avatarImageProcessing.storagePath(), avatarFileName);
    }

    /**
     * Get directory with images.
     * 
     * @return Path to directory.
     */
    public Path getImagesDirectory() {

        return Path.of(adImageProcessing.storagePath());
    }

    /**
     * Get image path.
     * 
     * @param imageFileName Image file name.
     * @return Full image path.
     */
    public Path getAdImagePath(String imageFileName) {

        return Path.of(adImageProcessing.storagePath(), imageFileName);
    }

    /**
     * Validate image.
     * 
     * @param verb Verb for exception messages.
     * @param image Uploaded image.
     * @param properties Image validation properties.
     * @throws TkNullArgumentException If any argument is null.
     * @throws TkUnsupportedMediaTypeException If image's MIME type is not allowed.
     * @throws TkSizeException If image's size is not allowed.
     */
    public static void validateImage(final String verb, final MultipartFile image,
            final ImageProcessingProperties properties) {

        if (image == null) {
            throw new TkNullArgumentException("image");
        }
        if (properties == null) {
            throw new TkNullArgumentException("properties");
        }

        final long imageSize = image.getSize();
        if (image.isEmpty() ||
                imageSize < properties.minFileSizeBytes()
                || imageSize > properties.maxFileSizeBytes()) {
            throw new TkSizeException(verb);
        }

        final String contentType = image.getContentType();
        if (contentType == null || contentType.isBlank() ||
                contentType.length() > CONTENT_TYPE_LENGTH_MAX) {
            throw new TkUnsupportedMediaTypeException("unknown");
        }

        try {
            MediaType.parseMediaType(contentType);
        } catch (Exception ex) {
            throw new TkUnsupportedMediaTypeException(contentType);
        }

        if (!properties.allowedMimeTypes().contains(contentType)) {
            throw new TkUnsupportedMediaTypeException(contentType);
        }
    }

    /**
     * Generate unique file name for uploaded image.
     * 
     * Call {@link #validateImage(String, MultipartFile, ImageProcessingProperties)} first!
     * 
     * @param image Uploaded image.
     * @return Generated unique file name.
     * @throws TkNullArgumentException If any argument is null.
     */
    public static String getImageUniqueFileName(final MultipartFile image) {

        if (image == null) {
            throw new TkNullArgumentException("image");
        }

        final String contentType = image.getContentType();
        if (contentType == null || contentType.isBlank() ||
                contentType.length() > CONTENT_TYPE_LENGTH_MAX) {
            throw new TkUnsupportedMediaTypeException("unknown");
        }

        final String ext = contentType.substring(contentType.lastIndexOf('/') + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String saveImageFile(final String verb, final MultipartFile image,
            final ImageProcessingProperties processingProperties) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        validateImage(verb, image, processingProperties);

        final String fileName = getImageUniqueFileName(image);
        final Path path = Path.of(processingProperties.storagePath(), fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);
            try (
                    InputStream is = image.getInputStream();
                    OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                    BufferedInputStream bis =
                            new BufferedInputStream(is, processingProperties.ioBufferSize());
                    BufferedOutputStream bos =
                            new BufferedOutputStream(os, processingProperties.ioBufferSize())) {
                bis.transferTo(bos);
            }
        } catch (Exception e) {
            throw new TkSavingMediaException(path.toString());
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return fileName;
    }

    /**
     * Save avatar file.
     * 
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveAvatarFile(final MultipartFile image) {

        return saveImageFile("avatar", image, avatarImageProcessing);
    }

    /**
     * Save ad's image file.
     * 
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveAdImageFile(final MultipartFile image) {

        return saveImageFile("ad image", image, adImageProcessing);
    }

    private void deleteImageFile(final String fileName,
            final ImageProcessingProperties properties) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (fileName != null && !fileName.isBlank()) {
            try {
                final Path path = Path.of(properties.storagePath(), fileName);
                Files.deleteIfExists(path);
            } catch (Exception ex) {
                throw new TkDeletingMediaException(fileName);
            }
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Delete avatar file.
     * 
     * @param fileName File name.
     */
    public void deleteAvatarImageFile(final String fileName) {

        deleteImageFile(fileName, avatarImageProcessing);
    }

    /**
     * Delete ad image file.
     * 
     * @param fileName File name.
     */
    public void deleteAdImageFile(final String fileName) {

        deleteImageFile(fileName, adImageProcessing);
    }
}
