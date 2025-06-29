package ru.spb.tksoft.ads.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.projection.AdResponseProjection;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.ImageRepository;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ads service.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdsService {

    private final Logger log = LoggerFactory.getLogger(AdsService.class);

    private final AdsServiceCached adsServiceCached;
    private final UserServiceCached userServiceCached;
    private final ResourceService resourceService;

    private final AdRepository adRepository;

    /**
     * Get a list of all ads.
     * 
     * @return DTO.
     */
    public AdsArrayResponseDto getAllAds() {

        // Extra: user id, image url
        final List<AdResponseProjection> projections = adRepository.findManyMinimal();
        if (projections.isEmpty()) {
            return new AdsArrayResponseDto(0, Set.of());
        }

        Set<AdResponseDto> responseSet = projections.stream()
                .map(projection -> AdMapper.toDto(resourceService, projection))
                .collect(Collectors.toSet());

        return AdMapper.toAdsDto(responseSet.size(), responseSet);
    }

    /**
     * Create new ad.
     * 
     * @param userDetails UserDetails implementation.
     * @param createAddDto Request DTO.
     * @return Response DTO.
     */
    @Transactional
    public AdResponseDto createAdd(final UserDetails userDetails,
            final CreateOrUpdateAdRequestDto createAddDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        final UserEntity user = userServiceCached.getUserEntityLazy(userDetails.getUsername());

        final AdEntity newAd = AdMapper.toEntity(createAddDto);
        newAd.setUser(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return AdMapper.toDto(resourceService, adRepository.save(newAd));
    }

    /**
     * Save ad image file.
     * 
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveImageFile(final MultipartFile image) {

        return resourceService.saveAdImageFile(image);
    }

    /**
     * Update ad image.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad ID.
     * @param fileName Filename.
     * @param fileSize File size.
     * @param contentType File type.
     */
    @Transactional
    public void updateAdImage(final UserDetails userDetails, long adId,
            final String fileName, long fileSize, final String contentType) {

        AdEntity ad = adsServiceCached.getAdEntityWithImage(userDetails.getUsername(), adId);

        // Planning to delete old ad image.
        String oldFileName = ad.getImage() == null ? "" : ad.getImage().getName();
        if (!oldFileName.isBlank()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_COMMITTED) {
                                resourceService.deleteAdImageFile(oldFileName);
                            }
                            if (status == STATUS_ROLLED_BACK) {
                                resourceService.deleteAdImageFile(fileName);
                            }
                        }
                    });
        }

        var image = new ImageEntity(fileName, (int) fileSize, contentType);
        image.setAd(ad);
        ad.setImage(image);

        adsServiceCached.clearCaches();
    }

    /**
     * Update ad user with given ID and owned by given user.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     * @param requestDto Request DTO.
     * @return Response DTO.
     */
    @Transactional
    public AdResponseDto updateAdInfo(UserDetails userDetails, long adId,
            CreateOrUpdateAdRequestDto requestDto) {

        AdEntity ad = adsServiceCached.getAdEntityWithImage(userDetails.getUsername(), adId);

        ad.setTitle(requestDto.getTitle());
        ad.setPrice(BigDecimal.valueOf(requestDto.getPrice()));
        ad.setDescription(requestDto.getDescription());

        adsServiceCached.clearCaches();
        return AdMapper.toDto(resourceService, ad);
    }

    /**
     * Remove ad with given id and owned by given user.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     */
    @Transactional
    public void deleteAd(UserDetails userDetails, long adId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        AdEntity ad = adsServiceCached.getAdEntityWithImage(userDetails.getUsername(), adId);

        // Planning to delete ad image.
        String imageFileName = ad.getImage() == null ? "" : ad.getImage().getName();
        if (!imageFileName.isBlank()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_COMMITTED) {
                                resourceService.deleteAdImageFile(imageFileName);
                            }
                        }
                    });
        }

        adRepository.delete(ad);
        adsServiceCached.clearCaches();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
