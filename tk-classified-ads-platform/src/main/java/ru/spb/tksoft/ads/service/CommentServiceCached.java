package ru.spb.tksoft.ads.service;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.mapper.CommentMapper;
import ru.spb.tksoft.ads.repository.CommentRepository;

/**
 * Comment service. Cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class CommentServiceCached {

    private final ResourceService resourceService;
    private final CommentRepository commentRepository;

        private final CacheManager cacheManager;

    private void clearCache(String name) {

        Cache cache = cacheManager.getCache(name);
        assert (cache != null);

        cache.clear();
    }

    /** Clear caches. */
    public void clearCaches() {

        clearCache("getComments");
    }

    /**
     * Get comments for given ad.
     * 
     * @param adId Ad id.
     * @return Response DTO.
     */
    @Cacheable(value = "getComments", key = "#adId")
    public CommentsArrayResponseDto getComments(final Long adId) {

        Set<CommentResponseDto> resultSet = commentRepository.findManyByAdId(adId).stream()
                .map(comment -> CommentMapper.toDto(resourceService, comment))
                .collect(Collectors.toSet());

        return CommentMapper.toDto(resultSet);
    }
}
