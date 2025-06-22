package ru.spb.tksoft.ads.service;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
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
public class AdsService {

    private final Logger log = LoggerFactory.getLogger(AdsService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AdRepository adRepository;

    /**
     * Get list of all ads.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    public AdsArrayResponseDto getAllAds(final UserDetails userDetails) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Get list of all ads.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdsArrayResponseDto(0, Collections.emptySet());
    }

    /**
     * Get list of all my ads.
     * 
     * @param me UserDetails implementation.
     * @return DTO.
     */
    public AdsArrayResponseDto getAdsMe(final UserDetails me) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (me == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Get list of all my ads.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdsArrayResponseDto(0, Collections.emptySet());
    }

    /**
     * Add new ad from logged user.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    public AdResponseDto addAd(final UserDetails userDetails,
            final CreateOrUpdateAdRequestDto createAddDto,
            final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Create new ad from logged user and load image.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdResponseDto();
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

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

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

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

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

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdExtendedResponseDto();
    }

    public AdResponseDto updateAds(UserDetails userDetails, long id,
            CreateOrUpdateAdRequestDto updateAdsDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdResponseDto();
    }

    public void removeAd(UserDetails userDetails, long id) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    public CommentResponseDto updateComment(final UserDetails userDetails, long adId,
            long commentId,
            final CreateOrUpdateCommentRequestDto updateCommentDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new CommentResponseDto();
    }

    public void deleteComment(final UserDetails userDetails, long adId, long commentId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    public void updateImage(final UserDetails userDetails, long id,
            final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new IllegalArgumentException("userDeatails must not be null");
        }

        // TODO: Implement this method.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
