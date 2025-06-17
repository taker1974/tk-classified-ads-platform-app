package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.AdsArrayResponseDto;
import ru.spb.tksoft.ads.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.lang.Collections;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    @NotNull
    private final UserService userService;

    /**
     * Get / - Get all ads.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение всех объявлений")
    @GetMapping("/")
    public AdsArrayResponseDto getAllAds(@AuthenticationPrincipal UserDetails userDetails) {

        return new AdsArrayResponseDto(0, Collections.emptySet());
    }

    /**
     * Get /me - Ads from authenticated user.
     * 
     * @return 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public AdsArrayResponseDto getAdsMe(@AuthenticationPrincipal UserDetails userDetails) {

        return new AdsArrayResponseDto(0, Collections.emptySet());
    }
}
