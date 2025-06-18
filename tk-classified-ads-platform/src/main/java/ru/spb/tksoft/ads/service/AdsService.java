package ru.spb.tksoft.ads.service;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
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

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final PasswordEncoder passwordEncoder;

    /**
     * Get list of all ads.
     * 
     * @param userDetails UserDetails implementation.
     * @return DTO.
     */
    @NotNull
    public AdsArrayResponseDto getAllAds(@NotNull final UserDetails userDetails) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

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
    @NotNull
    public AdsArrayResponseDto getAdsMe(@NotNull final UserDetails me) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

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
    @NotNull
    public AdResponseDto addAd(@NotNull final UserDetails userDetails,
            @NotNull final CreateOrUpdateAdRequestDto createAddDto,
            @NotNull final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

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
    @NotNull
    public CommentsArrayResponseDto getComments(@NotNull final UserDetails userDetails,
            final long id) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Get all comments for ad with given id.

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
    @NotNull
    public CommentResponseDto addComment(@NotNull final UserDetails userDetails,
            final long adId, @NotNull final CreateOrUpdateCommentRequestDto requestDto) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Add new comment to add.

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
    @NotNull
    public AdExtendedResponseDto getAds(@NotNull final UserDetails userDetails, final long adId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        // TODO: Add new comment to add.

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new AdExtendedResponseDto();
    }
}
