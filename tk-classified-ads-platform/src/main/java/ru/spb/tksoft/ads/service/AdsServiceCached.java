package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
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
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.ImageRepository;

/**
 * Ads service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdsServiceCached {

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

        clearCache("getAdsMe");
        clearCache("getAdImage");
        clearCache("getOwnAdEntity");
        clearCache("getAdExtended");
        clearCache("getOwnCommentEntity");
    }

    /**
     * Get a list of all my ads.
     * 
     * @param me UserDetails implementation.
     * @return DTO.
     */
    //@Cacheable(value = "getAdsMe", key = "#me.username")
    public AdsArrayResponseDto getAdsMe(final UserDetails me) {

        Set<AdResponseDto> responseSet = adRepository.findByUserName(me.getUsername()).stream()
                .map(adEntity -> AdMapper.toDto(resourceService, adEntity))
                .collect(Collectors.toSet());

        return AdMapper.toAdsDto(responseSet.size(), responseSet);
    }

    /**
     * Get ad image by id.
     * 
     * @param id Ad ID.
     * @return Image resource.
     */
    //@Cacheable(value = "getAdImage", key = "#id")
    public ResponseEntity<Resource> getAdImage(final Long id) {

        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new TkMediaNotFoundException(String.valueOf(id)));

        String filename = image.getName();
        if (filename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = resourceService.getAdImagePath(filename);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
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
    //@Cacheable(value = "getOwnAdEntity", key = "#userName + '_' + #adId")
    public AdEntity getOwnAdEntity(final String userName, final Long adId) {

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        if (!ad.getUser().getName().equals(userName)) {
            throw new TkAdNotOwnedException(adId);
        }

        return ad;
    }

    /**
     * Get info about ad with given id.
     * 
     * @param adId Ad id.
     * @return Response DTO.
     */
    //@Cacheable(value = "getAdExtended", key = "#adId")
    public AdExtendedResponseDto getAdExtended(Long adId) {

        AdEntity entity = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        return AdMapper.toExtendedDto(resourceService, entity);
    }

    /**
     * Get all comments for ad with given id.
     * 
     * @param adId Ad id.
     * @return Response DTO.
     */
    //@Cacheable(value = "getComments", key = "#adId")
    public CommentsArrayResponseDto getComments(Long adId) {

        Set<CommentResponseDto> responseSet = commentRepository.findAllByAd_Id(adId).stream()
                .map(commentEntity -> CommentMapper.toDto(resourceService, commentEntity))
                .collect(Collectors.toSet());

        return AdMapper.toCommentsDto(responseSet.size(), responseSet);
    }

    /**
     * Get info about comment with given id.
     * 
     * @param userName User name.
     * @param adId Ad id.
     * @param commentId Comment id.
     * @return Response DTO.
     */
    //@Cacheable(value = "getOwnCommentEntity", key = "#userName + '_' + #adId + '_' + #commentId")
    public CommentEntity getOwnCommentEntity(final String userName,
            final long adId, final long commentId) {

        CommentEntity comment = commentRepository.findByAdAndCommentWithEagerFetch(adId, commentId)
                .orElseThrow(() -> new TkCommentNotFoundException(adId));

        if (!comment.getUser().getName().equals(userName)) {
            throw new TkCommentNotOwnedException(commentId);
        }

        return comment;
    }
}
