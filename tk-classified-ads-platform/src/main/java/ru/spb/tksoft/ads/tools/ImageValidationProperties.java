package ru.spb.tksoft.ads.tools;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Image validation properties.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ConfigurationProperties(prefix = "validation.image")
public record ImageValidationProperties(
    
    long minFileSizeBytes,
    long maxFileSizeBytes,

    int minWidth,
    int minHeight,

    int maxWidth,
    int maxHeight,

    List<String> allowedMimeTypes
) {}