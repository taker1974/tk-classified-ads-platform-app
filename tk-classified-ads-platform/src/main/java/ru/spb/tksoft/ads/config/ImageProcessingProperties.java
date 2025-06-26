package ru.spb.tksoft.ads.config;

import java.util.List;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * Image processing properties.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public record ImageProcessingProperties(

        long minFileSizeBytes,
        long maxFileSizeBytes,

        int minWidth,
        int minHeight,

        int maxWidth,
        int maxHeight,

        List<String> allowedMimeTypes,

        int ioBufferSize,
        String urlBasePath,
        String storagePath) {

    @ConstructorBinding
    public ImageProcessingProperties {
        // ...
    }
}
