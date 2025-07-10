package ru.spb.tksoft.ads.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.projection.AdExtendedResponseProjection;
import ru.spb.tksoft.ads.projection.AdResponseProjection;
import ru.spb.tksoft.ads.repository.AdRepository;

/**
 * Ad service, cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdServiceCached {

    private final AdRepository adRepository;

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
        List<AdResponseProjection> projections =
                adRepository.findManyMinimalByName(userDetails.getUsername());
        if (projections.isEmpty()) {
            return new AdsArrayResponseDto(0, Set.of());
        }

        Set<AdResponseDto> responseSet = projections.stream()
                .map(projection -> AdMapper.toDto(resourceService, projection))
                .collect(Collectors.toSet());

        return AdMapper.toAdsDto(responseSet.size(), responseSet);
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
