package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkMediaNotFoundException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.repository.AdRepository;
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

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final ResourceService resourceService;

    /**
     * Get a list of all ads.
     * 
     * @return DTO.
     */
    public AdsArrayResponseDto getAllAds() {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        Set<AdResponseDto> responseSet = adRepository.findAll().stream()
                .map(adEntity -> AdMapper.toDto(resourceService, adEntity))
                .collect(Collectors.toSet());

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * Get a list of all my ads.
     * 
     * @param me UserDetails implementation.
     * @return DTO.
     */
    public AdsArrayResponseDto getAdsMe(final UserDetails me) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        Set<AdResponseDto> responseSet = adRepository.findByUserName(me.getUsername()).stream()
                .map(adEntity -> AdMapper.toDto(resourceService, adEntity))
                .collect(Collectors.toSet());

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * Get ad image by id.
     * 
     * @param id Ad ID.
     * @return Image resource.
     */
    public ResponseEntity<Resource> getAdImage(final long id) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

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

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);

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

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
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
    public void updateAdDb(long adId, final String fileName, long fileSize,
            final String contentType) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        List<ImageEntity> images = ad.getImages();
        assert (images != null);

        // Planning to delete old ad image.
        String oldFileName = AdMapper.getFirstImageFileName(ad);
        if (!oldFileName.isBlank()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_COMMITTED) {
                                resourceService.deleteAdImageFile(oldFileName);
                            }
                        }
                    });
        }

        ImageEntity image = new ImageEntity();
        image.setName(fileName);
        image.setSize((int) fileSize);
        image.setMediatype(contentType);

        ad.addImage(image);
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Get all comments for ad with given id.
     * 
     * @param userDetails UserDetails implementation.
     * @param id Ad id.
     * @return DTO.
     */
    public CommentsArrayResponseDto getComments(final UserDetails userDetails, long id) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new CommentsArrayResponseDto(0, Collections.emptySet());
    }

    /**
     * Add comment to ad with given id.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     * @param requestDto DTO.
     * @return Created DTO.
     */
    public CommentResponseDto addComment(final UserDetails userDetails, long adId,
            final CreateOrUpdateCommentRequestDto requestDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new CommentResponseDto();
    }

    /**
     * Get info about ad with given id.
     * 
     * @param userDetails UserDetails implementation.
     * @param adId Ad id.
     * @return DTO.
     */
    public AdExtendedResponseDto getAds(final UserDetails userDetails, long adId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdExtendedResponseDto();
    }

    public AdResponseDto updateAds(UserDetails userDetails, long id,
            CreateOrUpdateAdRequestDto updateAdsDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdResponseDto();
    }

    public void removeAd(UserDetails userDetails, long id) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    public CommentResponseDto updateComment(final UserDetails userDetails, long adId,
            long commentId,
            final CreateOrUpdateCommentRequestDto updateCommentDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new CommentResponseDto();
    }

    public void deleteComment(final UserDetails userDetails, long adId, long commentId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    public void updateImage(final UserDetails userDetails, long id,
            final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
