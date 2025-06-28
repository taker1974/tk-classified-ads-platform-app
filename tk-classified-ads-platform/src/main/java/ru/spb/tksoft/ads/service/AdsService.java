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
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.mapper.CommentMapper;
import ru.spb.tksoft.ads.projection.AdProjection;
import ru.spb.tksoft.ads.projection.ImageProjection;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;

    /**
     * Get a list of all ads.
     * 
     * @return DTO.
     */
    public AdsArrayResponseDto getAllAds() {

        final List<AdProjection> responseList = adRepository.findMany();
        if (responseList.isEmpty()) {
            return new AdsArrayResponseDto(0, Set.of());
        }

        final List<ImageProjection> imageList = imageRepository.findMany();

        Set<AdResponseDto> responseSet = responseList.stream()
                .map(projection -> )

        
        stream()
                .map(projection -> AdMapper.toDto(resourceService, projection))
                .collect(Collectors.toSet());

        return AdMapper.toAdsDto(responseSet.size(), responseSet);
    }

    /**
     * Create new ad.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    @Transactional
    public AdResponseDto createAdd(final UserDetails userDetails,
            final CreateOrUpdateAdRequestDto createAddDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        final String userName = userDetails.getUsername();
        final UserEntity user = userRepository.findOneByNameRaw(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

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
     * @param adId Ad ID.
     * @param fileName Filename.
     * @param fileSize File size.
     * @param contentType File type.
     * @throws TkAdNotFoundException Thrown when ad not found.
     */
    @Transactional
    public void updateAdImage(final UserDetails userDetails,
            long adId, final String fileName, long fileSize, final String contentType) {

        AdEntity ad = adsServiceCached.getOwnAdEntity(userDetails.getUsername(), adId);

        List<ImageEntity> images = ad.getImages();
        assert (images != null);

        // Planning to delete old ad image.
        String oldFileName = AdMapper.getFirstImageFileName(ad);
        if (oldFileName != null && !oldFileName.isBlank()) {
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

        ImageEntity image = new ImageEntity();
        image.setName(fileName);
        image.setSize((int) fileSize);
        image.setMediatype(contentType);

        ad.addImage(image);
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

        AdEntity ad = adsServiceCached.getOwnAdEntity(userDetails.getUsername(), adId);

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

        AdEntity ad = adsServiceCached.getOwnAdEntity(userDetails.getUsername(), adId);

        List<ImageEntity> images = ad.getImages();
        assert (images != null);

        // Planning to delete ad image.
        String imageFileName = AdMapper.getFirstImageFileName(ad);
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

    /**
     * Add comment to ad with given id.
     * 
     * @param adId Ad id.
     * @param requestDto DTO.
     * @return Response DTO.
     */
    @Transactional
    public CommentResponseDto addComment(long adId,
            final CreateOrUpdateCommentRequestDto requestDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        AdEntity ad;
        try {
            ad = adRepository.getReferenceById(adId);
        } catch (Exception ex) {
            throw new TkAdNotFoundException(adId);
        }

        long userId = ad.getUser().getId();
        UserEntity user;
        try {
            user = userRepository.getReferenceById(userId);
        } catch (Exception ex) {
            throw new TkUserNotFoundException(String.format("user [%s]", userId), false);
        }

        var comment = new CommentEntity(requestDto.getText());
        comment.setAd(ad);
        comment.setUser(user);

        CommentEntity savedComment;
        try {
            savedComment = commentRepository.save(comment);
        } catch (Exception ex) {
            LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, ex);
            throw ex;
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return CommentMapper.toDto(resourceService, savedComment);
    }

    /**
     * Update comment with given ad ID and comment ID.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     * @param commentId Comment id.
     * @param requestDto Request DTO.
     * @return Response DTO.
     */
    @Transactional
    public CommentResponseDto updateComment(final UserDetails userDetails,
            long adId, long commentId,
            final CreateOrUpdateCommentRequestDto requestDto) {

        CommentEntity entity =
                adsServiceCached.getOwnCommentEntity(userDetails.getUsername(), adId, commentId);
        entity.setText(requestDto.getText());

        adsServiceCached.clearCaches();
        return CommentMapper.toDto(resourceService, entity);
    }

    /**
     * Delete comment with given ad ID and comment ID.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     * @param commentId Comment id.
     */
    @Transactional
    public void deleteComment(final UserDetails userDetails,
            long adId, long commentId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        CommentEntity entity =
                adsServiceCached.getOwnCommentEntity(userDetails.getUsername(), adId, commentId);
        commentRepository.delete(entity);

        adsServiceCached.clearCaches();
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
