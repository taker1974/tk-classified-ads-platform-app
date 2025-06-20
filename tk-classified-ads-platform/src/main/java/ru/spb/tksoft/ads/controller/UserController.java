package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@Tag(name = "Пользователи")
@RequestMapping("/users")
public class UserController {

    @NotNull
    private final UserService userService;

    /**
     * POST /set_password - set new password endpoint.
     * 
     * Return 200/OK, 401/Unauthorized, 403/Forbidden.
     * 
     * @param newPasswordRequest - DTO for setting new password.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NewPasswordRequestDto newPasswordRequest) {

        userService.setPassword(userDetails, newPasswordRequest);
    }

    /**
     * GET /me - Info about authenticated user.
     * 
     * Return 200/OK, 401/Unauthorized.
     * 
     * @return Response DTO.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public UserResponseDto getUser(@AuthenticationPrincipal UserDetails userDetails) {

        return userService.findUserByName(userDetails.getUsername());
    }

    /**
     * PATCH /me - Update authenticated user.
     * 
     * Return 200/OK, 401/Unauthorized.
     * 
     * @param updateRequest - Update request DTO.
     * @return Response DTO.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public UpdateUserResponseDto updateUser(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequestDto updateRequest) {

        return userService.updateUser(userDetails.getUsername(), updateRequest);
    }
}
