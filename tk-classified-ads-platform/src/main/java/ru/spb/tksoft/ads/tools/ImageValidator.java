package ru.spb.tksoft.ads.tools;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkSizeException;
import ru.spb.tksoft.ads.exception.TkUnsupportedMediaTypeException;

/**
 * Image validator.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class ImageValidator {

    /** Maximul length of content-type string. */
    public static final int CONTENTTYPE_LENGTH_MAX = 128;

    /** Private constructor. */
    private ImageValidator() {}

    /**
     * Validate image.
     * 
     * @param image Uploaded image.
     * @param properties Image validation properties.
     * @throws TkNullArgumentException If any argument is null.
     * @throws TkUnsupportedMediaTypeException If image's MIME type is not allowed.
     * @throws TkSizeException If image's size is not allowed.
     */
    public static void validateImage(final MultipartFile image,
            final ImageValidationProperties properties) {

        if (image == null) {
            throw new TkNullArgumentException("image");
        }

        final long imageSize = image.getSize();
        if (image.isEmpty() ||
                imageSize < properties.minFileSizeBytes()
                || imageSize > properties.maxFileSizeBytes()) {
            throw new TkSizeException("avatar size");
        }

        final String contentType = image.getContentType();
        if (contentType == null || contentType.isBlank() || contentType.length() > 128) {
            throw new TkUnsupportedMediaTypeException("unknown");
        }

        if (!properties.allowedMimeTypes().contains(contentType)) {
            throw new TkUnsupportedMediaTypeException(contentType);
        }
    }

    /**
     * Generate unique file name for uploaded image.
     * 
     * Call {@link #validateImage(MultipartFile, ImageValidationProperties)} first!
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
        if (contentType == null) {
            throw new TkNullArgumentException("contentType");
        }

        final String ext = contentType.substring(contentType.lastIndexOf('/') + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }
}
