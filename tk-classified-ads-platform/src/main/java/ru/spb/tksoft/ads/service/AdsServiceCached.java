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
import ru.spb.tksoft.ads.projection.AdResponseProjection;
import ru.spb.tksoft.ads.projection.CommentProjection;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.ImageRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ads service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdsServiceCached {

    private final Logger log = LoggerFactory.getLogger(AdsServiceCached.class);

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
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

        clearCache("getMyAds");
        clearCache("getAdImage");
        clearCache("getAdEntityWithImage");
        clearCache("getAdExtended");
        clearCache("getCommentEntity");
    }

    /**
     * Get a list of all my ads.
     * 
     * @param me UserDetails implementation.
     * @return DTO.
     */
    @Cacheable(value = "getMyAds", key = "#me.username")
    public AdsArrayResponseDto getMyAds(final UserDetails me) {

        // Extra: user id, image url
        final List<AdResponseProjection> projections =
                adRepository.findManyMinimal(me.getUsername());
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
     * @param id Ad ID.
     * @return Image resource.
     */
    @Cacheable(value = "getAdImage", key = "#id")
    public ResponseEntity<Resource> getAdImage(final Long id) {

        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new TkMediaNotFoundException(String.valueOf(id)));

        String filename = image.getName();

        if (filename == null || filename.isBlank()) {
            LogEx.error(log, LogEx.getThisMethodName(),
                    "Ad " + id + ": " + "image file not set");
            return resourceService.getDefaultAdImage();
        }

        Path filePath = resourceService.getAdImagePath(filename);
        if (!Files.exists(filePath)) {
            LogEx.error(log, LogEx.getThisMethodName(),
                    "Ad " + id + ": " + "image file \"" + filename + "\" not found");
            return resourceService.getDefaultAdImage();
        }

        Resource resource = new PathResource(filePath);
        MediaType mediaType = MediaType.parseMediaType(image.getMediatype());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    /**
     * Get info about ad with given id.
     * 
     * @param userName User name.
     * @param adId Ad id.
     * @return Response DTO.
     */
    @Cacheable(value = "getAdEntityWithImage", key = "#userName + '_' + #adId")
    public AdEntity getAdEntityWithImage(final String userName, final Long adId) {

        return adRepository.findOneByUserNameAndAdIdWithImage(userName, adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf(adId)));
    }

    // /**
    // * Get info about ad with given id.
    // *
    // * @param adId Ad id.
    // * @return Response DTO.
    // */
    // @Cacheable(value = "getAdExtended", key = "#adId")
    // public AdExtendedResponseDto getAdExtended(Long adId) {

    // AdEntity entity = adRepository.findById(adId)
    // .orElseThrow(() -> new TkAdNotFoundException(adId));

    // return AdMapper.toExtendedDto(resourceService, entity);
    // }

    // /**
    // * Get all comments for ad with given id.
    // *
    // * @param adId Ad id.
    // * @return Response DTO.
    // */
    // @Cacheable(value = "getComments", key = "#adId")
    // public CommentsArrayResponseDto getComments(Long adId) {

    // Set<CommentResponseDto> responseSet = commentRepository.findManyByAdId(adId).stream()
    // .map(projection -> CommentMapper.toDto(resourceService, projection))
    // .collect(Collectors.toSet());

    // return AdMapper.toCommentsDto(responseSet.size(), responseSet);
    // }

    // /**
    // * Get comment by id.
    // *
    // * @param commentId Comment id.
    // * @return CommentEntity.
    // */
    // @Cacheable(value = "getCommentEntity", key = "#commentId")
    // public CommentEntity getCommentEntity(final long commentId) {

    // return commentRepository.findOneById(commentId)
    // .orElseThrow(() -> new TkCommentNotFoundException(String.valueOf(commentId)));
    // }
}
