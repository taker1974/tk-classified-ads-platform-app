package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkAdNotOwnedException;
import ru.spb.tksoft.ads.exception.TkCommentNotFoundException;
import ru.spb.tksoft.ads.exception.TkCommentNotOwnedException;
import ru.spb.tksoft.ads.exception.TkMediaNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.mapper.CommentMapper;
import ru.spb.tksoft.ads.projection.AdExtendedResponseProjection;
import ru.spb.tksoft.ads.projection.AdResponseProjection;
import ru.spb.tksoft.ads.projection.CommentProjection;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.ImageRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ad service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdServiceCached {

    private final Logger log = LoggerFactory.getLogger(AdServiceCached.class);

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;

    private final ResourceService resourceService;

    private final CacheManager cacheManager;

    private void clearCache(String name) {

        Cache cache = cacheManager.getCache(name);
        assert (cache != null);

        cache.clear();
    }

    /** Clear caches. */
    public void clearCaches() {

        clearCache("getAds");
        clearCache("getAdImage");
        clearCache("getAdInfo");
    }

    /**
     * Get a list of all my ads.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    @Cacheable(value = "getAds", key = "#userDetails.getUsername()")
    public AdsArrayResponseDto getAds(final UserDetails userDetails) {

        // Extra: user id, image url
        List<AdResponseProjection> projections = adRepository.findManyMinimal(userDetails.getUsername());
        if (projections.isEmpty()) {
            return new AdsArrayResponseDto(0, Set.of());
        }

        Set<AdResponseDto> responseSet = projections.stream()
                .map(projection -> AdMapper.toDto(resourceService, projection))
                .collect(Collectors.toSet());

        return AdMapper.toAdsDto(responseSet.size(), responseSet);
    }

    /**
     * Get ad image by id.
     * 
     * @param adId Ad ID.
     * @return Image resource.
     */
    @Cacheable(value = "getAdImage", key = "#adId")
    public ResponseEntity<Resource> getAdImage(final Long adId) {

        ImageEntity image = imageRepository.findById(adId)
                .orElseThrow(() -> new TkMediaNotFoundException(String.valueOf(adId)));

        String filename = image.getName();

        if (filename == null || filename.isBlank()) {
            LogEx.error(log, LogEx.getThisMethodName(),
                    "Ad " + adId + ": " + "image file not set");
            return resourceService.getDefaultAdImage();
        }

        Path filePath = resourceService.getAdImagePath(filename);
        if (!Files.exists(filePath)) {
            LogEx.error(log, LogEx.getThisMethodName(),
                    "Ad " + adId + ": " + "image file \"" + filename + "\" not found");
            return resourceService.getDefaultAdImage();
        }

        Resource resource = new PathResource(filePath);
        MediaType mediaType = MediaType.parseMediaType(image.getMediatype());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    /**
     * Get ad info.
     *
     * @param adId Ad id.
     * @return Response DTO.
     */
    @Cacheable(value = "getAdInfo", key = "#adId")
    public AdExtendedResponseDto getAdInfo(final Long adId) {

        AdExtendedResponseProjection projection = adRepository.findOneExtended(adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf(adId)));

        return AdMapper.toDto(resourceService, projection);
    }
}
