package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import ru.spb.tksoft.ads.service.AdsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
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
     * Get / - Get all ads.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение всех объявлений")
    @GetMapping()
    @NotNull
    public AdsArrayResponseDto getAllAds(@AuthenticationPrincipal UserDetails userDetails) {

        return adsService.getAllAds(userDetails);
    }

    /**
     * Get /me - Ads from authenticated user.
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
     * Post / - Create new ad.
     * 
     * @return 201/CREATED, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data")
    @NotNull
    public AdResponseDto addAd(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestPart("properties") CreateOrUpdateAdRequestDto createAddDto,
            @RequestPart("image") MultipartFile image) {

        return adsService.addAd(userDetails, createAddDto, image);
    }

    /**
     * Get /{id}/comments - get comments of ad.
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
     * Post /{id}/comments - create new comment for ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    @NotNull
    public CommentResponseDto addComment(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long id,
            @Valid @RequestBody CreateOrUpdateCommentRequestDto createCommentDto) {

        return adsService.addComment(userDetails, id, createCommentDto);
    }
}
