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
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.projection.AdResponseProjection;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ads service.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AdService {

    private final Logger log = LoggerFactory.getLogger(AdService.class);

    private final AdServiceCached adsServiceCached;
    private final UserServiceCached userServiceCached;
    private final ResourceService resourceService;

    private final AdRepository adRepository;
    private final UserRepository userRepository;

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
     * Create new ad entity.
     * 
     * @param userDetails UserDetails implementation.
     * @param createAddDto Request DTO.
     * @param fileName Saved file name.
     * @param fileSize Saved file size.
     * @param contentType Saved file type.
     * @return Newly created ad entity.
     */
    public AdEntity createAdEntity(final UserDetails userDetails,
            final CreateOrUpdateAdRequestDto createAddDto,
            final String fileName, final int fileSize, final String contentType) {

        final String userName = userDetails.getUsername();
        final UserEntity user = userRepository.findOneByNameLazy(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        final AdEntity newAd = AdMapper.toEntity(createAddDto);
        newAd.setUser(user);

        var imageEntity = new ImageEntity(fileName, fileSize, contentType);
        newAd.setImage(imageEntity);

        return newAd;
    }

    /**
     * Save new ad entity inside transaction.
     * 
     * @param adEntity Ad entity.
     * @param savedFileName Saved file name.
     * @return Saved ad entity.
     */
    @Transactional
    public AdEntity saveAdEntity(final AdEntity adEntity, final String savedFileName) {

        if (savedFileName != null && !savedFileName.isBlank()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_ROLLED_BACK) {
                                resourceService.deleteAdImageFile(savedFileName);
                            }
                        }
                    });
        }

        return adRepository.save(adEntity);
    }

    /** AdEntity to DTO. */
    public AdResponseDto getCreatedAd(final AdEntity entity) {

        return AdMapper.toDto(resourceService, entity);
    }

    /**
     * Update ad image.
     * 
     * @param userName User name.
     * @param adId Ad ID.
     * @param fileName Filename.
     * @param fileSize File size.
     * @param contentType File type.
     */
    @Transactional
    public void updateImageEntity(final String userName, Long adId,
            final String fileName, Long fileSize, final String contentType) {

        AdEntity ad = adRepository.findOneByUserNameAndAdId(userName, adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf( adId)));

        // Planning to delete old ad image file.
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

        ImageEntity image = ad.getImage();

        image.setName(fileName);
        image.setSize(fileSize.intValue());
        image.setMediatype(contentType);

        adsServiceCached.clearCaches();
    }

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

        AdEntity ad = adRepository
                .findOneByUserNameAndAdId(userDetails.getUsername(), adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf(adId)));

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

        AdEntity ad = adRepository
                .findOneByUserNameAndAdId(userDetails.getUsername(), adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf(adId)));

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
