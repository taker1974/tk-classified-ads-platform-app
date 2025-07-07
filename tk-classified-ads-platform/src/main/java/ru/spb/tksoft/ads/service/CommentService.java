package ru.spb.tksoft.ads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkCommentNotFoundException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.CommentMapper;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Comment service.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final ResourceService resourceService;

    private final CommentServiceCached commentServiceCached;

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * Create comment entity.
     * 
     * @param userName User name.
     * @param adId Ad id.
     * @param requestDto DTO.
     * @return Response DTO.
     */
    public CommentEntity createCommentEntity(final String userName,
            final Long adId, final CreateOrUpdateCommentRequestDto requestDto) {

        final AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new TkAdNotFoundException(String.valueOf(adId)));

        final UserEntity user = userRepository.findOneByNameLazy(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        var comment = new CommentEntity(requestDto.getText());
        comment.setAd(ad);
        comment.setUser(user);
        comment.setText(requestDto.getText());

        return comment;
    }

    /**
     * Add new comment.
     * 
     * @param entity Comment entity.
     * @return Response DTO.
     */
    @Transactional
    public CommentResponseDto addComment(final CommentEntity entity) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_COMMITTED) {
                            commentServiceCached.clearCaches();
                        }
                    }
                });

        CommentEntity savedComment = commentRepository.save(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return CommentMapper.toDto(resourceService, savedComment);
    }

    /**
     * Update comment with given ad ID and comment ID.
     *
     * @param userName User name.
     * @param adId Ad id.
     */
    @Transactional
    public CommentResponseDto updateComment(final String userName,
            Long adId, Long commentId, final CreateOrUpdateCommentRequestDto requestDto) {

        CommentEntity comment = commentRepository.findOneByUserExact(userName, adId, commentId)
                .orElseThrow(() -> new TkCommentNotFoundException(
                        "Comment from " + userName + " with id " + commentId + " not found"));

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_COMMITTED) {
                            commentServiceCached.clearCaches();
                        }
                    }
                });

        comment.setText(requestDto.getText());
        return CommentMapper.toDto(resourceService, comment);
    }

    /**
     * Delete comment with given ad ID and comment ID.
     *
     * @param userName User name.
     * @param adId Ad id.
     * @param commentId Comment id.
     */
    @Transactional
    public void deleteComment(final String userName, Long adId, Long commentId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        CommentEntity comment = commentRepository.findOneByUserExact(userName, adId, commentId)
                .orElseThrow(() -> new TkCommentNotFoundException(
                        "Comment from " + userName + " with id " + commentId + " not found"));

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_COMMITTED) {
                            commentServiceCached.clearCaches();
                        }
                    }
                });

        commentRepository.delete(comment);
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
