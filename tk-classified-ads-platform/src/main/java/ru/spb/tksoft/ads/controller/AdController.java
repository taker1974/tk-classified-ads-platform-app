package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.service.AdService;
import ru.spb.tksoft.ads.service.AdServiceCached;
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
@Tag(name = "Объявления: основная информация")
@RequestMapping("/ads")
public class AdController {

    private final AdService adsService;
    private final AdServiceCached adsServiceCached;

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
     * Get all ads of authenticated user.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    @NotNull
    public AdsArrayResponseDto getAdsMe(@AuthenticationPrincipal UserDetails userDetails) {

        return adsServiceCached.getAds(userDetails);
    }

    /**
     * Get ad image.
     * 
     * Returns 200/OK, 404/Not Found.
     * 
     * @param adId - User ID.
     * @return Image resource.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/image/{adId}")
    public ResponseEntity<Resource> getAdImage(@PathVariable(required = true) long adId) {

        return adsService.getAdImage(adId);
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
            @NotNull @Valid @RequestPart("properties") CreateOrUpdateAdRequestDto createAdRequest,
            @NotNull @RequestPart("image") MultipartFile image) {

        final String savedFileName = adsService.saveImageFile(image);
        final AdEntity adEntity = adsService.createAdEntity(userDetails.getUsername(), createAdRequest,
                savedFileName, (int) image.getSize(), image.getContentType());
        final AdEntity savedAdEntity = adsService.saveAdEntity(adEntity, savedFileName);
        return adsService.getCreatedAd(savedAdEntity);
    }

    /**
     * Get information about ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{adId}")
    @NotNull
    public AdExtendedResponseDto getAdInfo(@PathVariable(required = true) long adId) {

        return adsServiceCached.getAdInfo(adId);
    }

    /**
     * Update ad image.
     * 
     * Returns 200/OK, 401/Unauthorized, 403/FORBIDDEN, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping(value = "/{adId}/image", consumes = "multipart/form-data")
    public void updateImage(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @NotNull @RequestPart("image") MultipartFile image) {

        final String savedFileName = adsService.saveImageFile(image);
        adsService.updateImageEntity(userDetails.getUsername(), adId,
                savedFileName, image.getSize(), image.getContentType());
    }

    /**
     * Update ad.
     * 
     * @return 200/OK, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{adId}")
    @NotNull
    public AdResponseDto updateAds(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId,
            @NotNull @Valid @RequestBody CreateOrUpdateAdRequestDto updateAdsDto) {

        return adsService.updateAdInfo(userDetails, adId, updateAdsDto);
    }

    /**
     * Delete ad.
     * 
     * Returns 204/NO_CONTENT, 401/Unauthorized, 404/NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{adId}")
    public void removeAd(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(required = true) long adId) {

        adsService.deleteAd(userDetails, adId);
    }
}
