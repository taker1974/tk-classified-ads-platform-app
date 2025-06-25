package ru.spb.tksoft.ads.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
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
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.entity.ImageEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkAdNotOwnedException;
import ru.spb.tksoft.ads.exception.TkCommentNotFoundException;
import ru.spb.tksoft.ads.exception.TkCommentNotOwnedException;
import ru.spb.tksoft.ads.exception.TkMediaNotFoundException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.AdMapper;
import ru.spb.tksoft.ads.mapper.CommentMapper;
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

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;

    private final ResourceService resourceService;

    /**
     * Get a list of all ads.
     * 
     * @return DTO.
     */
    public AdsArrayResponseDto getAllAds() {

        Set<AdResponseDto> responseSet = adRepository.findAll().stream()
                .map(adEntity -> AdMapper.toDto(resourceService, adEntity))
                .collect(Collectors.toSet());

        return new AdsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * Get a list of all my ads.
     * 
     * @param me UserDetails implementation.
     * @return DTO.
     */
    public AdsArrayResponseDto getAdsMe(final UserDetails me) {

        Set<AdResponseDto> responseSet = adRepository.findByUserName(me.getUsername()).stream()
                .map(adEntity -> AdMapper.toDto(resourceService, adEntity))
                .collect(Collectors.toSet());

        return new AdsArrayResponseDto(responseSet.size(), responseSet);
    }

    /**
     * Get ad image by id.
     * 
     * @param id Ad ID.
     * @return Image resource.
     */
    public ResponseEntity<Resource> getAdImage(final long id) {

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
     * Create new ad.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    @Transactional
    public AdResponseDto createAdd(final UserDetails userDetails,
            final CreateOrUpdateAdRequestDto createAddDto) {

        final String userName = userDetails.getUsername();
        final UserEntity user = userRepository.findOneByNameRaw(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        final AdEntity newAd = AdMapper.toEntity(createAddDto);
        newAd.setUser(user);

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

    private AdEntity getOwnAdEntity(final String userName, final long adId) {

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        if (!ad.getUser().getName().equals(userName)) {
            throw new TkAdNotOwnedException(adId);
        }

        return ad;
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
    public void updateAdDb(final UserDetails userDetails,
            long adId, final String fileName, long fileSize, final String contentType) {

        AdEntity ad = getOwnAdEntity(userDetails.getUsername(), adId);

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
    }

    /**
     * Get info about ad with given id.
     * 
     * @param adId Ad id.
     * @return Response DTO.
     */
    public AdExtendedResponseDto getAdExtended(long adId) {

        AdEntity entity = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        return AdMapper.toExtendedDto(resourceService, entity);
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
    public AdResponseDto updateAd(UserDetails userDetails, long adId,
            CreateOrUpdateAdRequestDto requestDto) {

        AdEntity ad = getOwnAdEntity(userDetails.getUsername(), adId);

        ad.setTitle(requestDto.getTitle());
        ad.setPrice(BigDecimal.valueOf(requestDto.getPrice()));
        ad.setDescription(requestDto.getDescription());

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

        AdEntity ad = getOwnAdEntity(userDetails.getUsername(), adId);

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

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(adId));

        CommentEntity comment = new CommentEntity(requestDto.getText());
        comment.setAd(ad);
        comment.setUser(ad.getUser());
        CommentEntity savedComment = commentRepository.save(comment);

        return CommentMapper.toDto(resourceService, savedComment);
    }

    /**
     * Get all comments for ad with given id.
     * 
     * @param adId Ad id.
     * @return Response DTO.
     */
    public CommentsArrayResponseDto getComments(long adId) {

        Set<CommentResponseDto> responseSet = commentRepository.findAllByAd_Id(adId).stream()
                .map(commentEntity -> CommentMapper.toDto(resourceService, commentEntity))
                .collect(Collectors.toSet());

        return new CommentsArrayResponseDto(responseSet.size(), responseSet);
    }

    private CommentEntity getOwnCommentEntity(final String userName,
            final long adId, final long commentId) {

        CommentEntity comment = commentRepository.findByAdAndCommentWithEagerFetch(adId, commentId)
                .orElseThrow(() -> new TkCommentNotFoundException(adId));

        if (!comment.getUser().getName().equals(userName)) {
            throw new TkCommentNotOwnedException(commentId);
        }

        return comment;
    }

    @Transactional
    public CommentResponseDto updateComment(final UserDetails userDetails,
            long adId, long commentId,
            final CreateOrUpdateCommentRequestDto requestDto) {

        CommentEntity entity = getOwnCommentEntity(userDetails.getUsername(), adId, commentId);
        entity.setText(requestDto.getText());
        return CommentMapper.toDto(resourceService, entity);
    }

    @Transactional
    public void deleteComment(final UserDetails userDetails,
            long adId, long commentId) {

        CommentEntity entity = getOwnCommentEntity(userDetails.getUsername(), adId, commentId);
        commentRepository.delete(entity);
    }
}
