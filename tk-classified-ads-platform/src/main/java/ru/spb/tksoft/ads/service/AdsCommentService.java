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
public class AdsCommentService {

    private final Logger log = LoggerFactory.getLogger(AdsCommentService.class);

    private final AdsServiceCached adsServiceCached;
    private final ResourceService resourceService;

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

//    /**
//      * Add comment to ad with given id.
//      * 
//      * @param adId Ad id.
//      * @param requestDto DTO.
//      * @return Response DTO.
//      */
//     @Transactional
//     public CommentResponseDto addComment(long adId,
//             final CreateOrUpdateCommentRequestDto requestDto) {

//         LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

//         AdEntity ad;
//         try {
//             ad = adRepository.getReferenceById(adId);
//         } catch (Exception ex) {
//             throw new TkAdNotFoundException(adId);
//         }

//         long userId = ad.getUser().getId();
//         UserEntity user;
//         try {
//             user = userRepository.getReferenceById(userId);
//         } catch (Exception ex) {
//             throw new TkUserNotFoundException(String.format("user [%s]", userId), false);
//         }

//         var comment = new CommentEntity(requestDto.getText());
//         comment.setAd(ad);
//         comment.setUser(user);

//         CommentEntity savedComment;
//         try {
//             savedComment = commentRepository.save(comment);
//         } catch (Exception ex) {
//             LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, ex);
//             throw ex;
//         }

//         LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
//         return CommentMapper.toDto(resourceService, savedComment);
//     }

//     /**
//      * Update comment with given ad ID and comment ID.
//      * 
//      * @param userDetails UserDetails implementation.
//      * @param adId Ad id.
//      * @param commentId Comment id.
//      * @param requestDto Request DTO.
//      * @return Response DTO.
//      */
//     @Transactional
//     public CommentResponseDto updateComment(final UserDetails userDetails,
//             long adId, long commentId,
//             final CreateOrUpdateCommentRequestDto requestDto) {

//         CommentEntity entity =
//                 adsServiceCached.getOwnCommentEntity(userDetails.getUsername(), adId, commentId);
//         entity.setText(requestDto.getText());

//         adsServiceCached.clearCaches();
//         return CommentMapper.toDto(resourceService, entity);
//     }

//     /**
//      * Delete comment with given ad ID and comment ID.
//      * 
//      * @param userDetails UserDetails implementation.
//      * @param adId Ad id.
//      * @param commentId Comment id.
//      */
//     @Transactional
//     public void deleteComment(final UserDetails userDetails,
//             long adId, long commentId) {

//         LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

//         commentRepository.delete(adsServiceCached.getCommentEntity(commentId));
//         adsServiceCached.clearCaches();
        
//         LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
//     }
}
