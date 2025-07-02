package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.service.CommentService;
import ru.spb.tksoft.ads.service.CommentServiceCached;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления: комментарии")
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;
    private final CommentServiceCached commentServiceCached;

    /**
     * Create new comment.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{adId}/comments")
    @NotNull
    public CommentResponseDto addComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @NotNull @Valid @RequestBody CreateOrUpdateCommentRequestDto createCommentDto) {

        final CommentEntity entity = commentService.createCommentEntity(userDetails.getUsername(),
                adId, createCommentDto);
        return commentService.addComment(entity);
    }

    /**
     * Get all comments.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{adId}/comments")
    @NotNull
    public CommentsArrayResponseDto getComments(@PathVariable(required = true) long adId) {

        return commentServiceCached.getComments(Long.valueOf(adId));
    }

    /**
     * Update comment.
     * 
     * @return 200/OK, 401/Unauthorized, 403/FORBIDDEN, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{adId}/comments/{commentId}")
    @NotNull
    public CommentResponseDto updateComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @PathVariable(required = true) long commentId,
            @NotNull @Valid @RequestBody CreateOrUpdateCommentRequestDto requestDto) {

        return commentService.updateComment(userDetails.getUsername(),
                adId, commentId, requestDto);
    }

    /**
     * Delete comment.
     * 
     * Returns 200/OK, 401/Unauthorized, 403/FORBIDDEN, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @PathVariable(required = true) long commentId) {

        commentService.deleteComment(userDetails.getUsername(), adId, commentId);
    }
}
