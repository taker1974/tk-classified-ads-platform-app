package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.service.UserService;
import ru.spb.tksoft.ads.service.UserServiceCached;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Tag(name = "Пользователи")
@RequestMapping("/users")
public class UserController {

    @NotNull
    private final UserService userService;

    @NotNull
    private final UserServiceCached userServiceCached;

    /**
     * Set new password endpoint.
     * 
     * Return 200/OK, 401/Unauthorized, 403/Forbidden.
     * 
     * @param newPasswordRequest - DTO for setting new password.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление пароля")
    @PatchMapping("/set_password")
    public void setPassword(@AuthenticationPrincipal UserDetails userDetails,
            @NotNull @Valid @RequestBody NewPasswordRequestDto newPasswordRequest) {

        userService.setPassword(userDetails, newPasswordRequest);
    }

    /**
     * Info about authenticated user.
     * 
     * Return 200/OK, 401/Unauthorized.
     * 
     * @return Response DTO.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    @NotNull
    public UserResponseDto getUser(@AuthenticationPrincipal UserDetails userDetails) {

        return userServiceCached.getUser(userDetails.getUsername());
    }

    /**
     * Update authenticated user.
     * 
     * Return 200/OK, 401/Unauthorized.
     * 
     * @param updateRequest - Update request DTO.
     * @return Response DTO.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    @NotNull
    public UpdateUserResponseDto updateUser(@AuthenticationPrincipal UserDetails userDetails,
            @NotNull @Valid @RequestBody UpdateUserRequestDto updateRequest) {

            return userService.updateUser(userDetails.getUsername(), updateRequest);
    }

    /**
     * Update avatar.
     * 
     * Returns 200/OK, 401/Unauthorized.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public void updateUserImage(@AuthenticationPrincipal UserDetails userDetails,
            @NotNull @RequestPart("image") MultipartFile image) {

        final String fileName = userService.saveAvatarFile(image);
        userService.updateAvatarDb(userDetails.getUsername(),
                fileName, image.getSize(), image.getContentType());
    }

    /**
     * Get avatar by link.
     * 
     * Returns 200/OK, 404/Not Found.
     * 
     * @param userId - User ID.
     * @return Image resource.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/avatar/{userId}")
    public ResponseEntity<Resource> getAvatar(@PathVariable(required = true) long userId) {

        return userServiceCached.getAvatar(userId);
    }
}
