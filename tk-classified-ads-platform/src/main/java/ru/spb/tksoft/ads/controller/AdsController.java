package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.service.AdsService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    @NotNull
    private final AdsService adsService;

    /**
     * Get all ads.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение всех объявлений")
    @GetMapping()
    @NotNull
    public AdsArrayResponseDto getAllAds() {

        return adsService.getAllAds();
    }

    /**
     * Ads from authenticated user.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    @NotNull
    public AdsArrayResponseDto getAdsMe(@AuthenticationPrincipal UserDetails userDetails) {

        return adsService.getAdsMe(userDetails);
    }

    /**
     * Get ad image by link.
     * 
     * Returns 200/OK, 404/Not Found.
     * 
     * @param adId - User ID.
     * @return Image resource.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getAdImage(@PathVariable(required = true) long id) {

        return adsService.getAdImage(id);
    }

    /**
     * Create new ad.
     * 
     * @return 201/CREATED, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data")
    @NotNull
    public AdResponseDto addAd(@AuthenticationPrincipal UserDetails userDetails,
            @NotNull @Valid @RequestPart("properties") CreateOrUpdateAdRequestDto createAddDto,
            @NotNull @RequestPart("image") MultipartFile image) {

        // Plan:
        // - create new ad in db; if ok:
        // - update ad image (exactly the same as UserController.updateUserImage).

        AdResponseDto dto = adsService.createAdd(userDetails, createAddDto);
        final String fileName = adsService.saveImageFile(image);

        adsService.updateAdDb(dto.getId(),
                fileName, image.getSize(), image.getContentType());

        return dto;
    }

    /**
     * Get comments of ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    @NotNull
    public CommentsArrayResponseDto getComments(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id) {

        return adsService.getComments(userDetails, id);
    }

    /**
     * Create new comment for ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    @NotNull
    public CommentResponseDto addComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id,
            @NotNull @Valid @RequestBody CreateOrUpdateCommentRequestDto createCommentDto) {

        return adsService.addComment(userDetails, id, createCommentDto);
    }

    /**
     * Get information about ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    @NotNull
    public AdExtendedResponseDto getAds(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id) {

        return adsService.getAds(userDetails, id);
    }

    /**
     * Update ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    @NotNull
    public AdResponseDto updateAds(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id,
            @NotNull @Valid @RequestBody CreateOrUpdateAdRequestDto updateAdsDto) {

        return adsService.updateAds(userDetails, id, updateAdsDto);
    }

    /**
     * Delete ad.
     * 
     * Returns 204/NO_CONTENT, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Обновление информации об объявлении")
    @DeleteMapping("/{id}")
    public void removeAd(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id) {

        adsService.removeAd(userDetails, id);
    }

    /**
     * Update comment of ad.
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
            @NotNull @Valid @RequestBody CreateOrUpdateCommentRequestDto updateCommentDto) {

        return adsService.updateComment(userDetails, adId, commentId, updateCommentDto);
    }

    /**
     * Delete comment of ad.
     * 
     * Returns 200/OK, 401/Unauthorized, 403/FORBIDDEN, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @PathVariable(required = true) long commentId) {

        adsService.deleteComment(userDetails, adId, commentId);
    }

    /**
     * Update image of ad.
     * 
     * Returns 200/OK, 401/Unauthorized, 403/FORBIDDEN, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public void updateImage(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id,
            @NotNull @RequestPart("image") MultipartFile image) {

        adsService.updateImage(userDetails, id, image);
    }
}
