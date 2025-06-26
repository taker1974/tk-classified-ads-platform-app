package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @NotNull
    private final AuthService authService;

    /**
     * POST /register - Registration endpoint.
     * 
     * @RequestBody - RegisterRequestDto - object with user registration data.
     * @return ResponseEntity<Void> - 201 Created if registration is successful, 400 Bad Request if
     *         registration fails.
     */
    @Operation(summary = "Регистрация пользователя")
    @Tag(name = "Регистрация")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @NotNull @Valid @RequestBody RegisterRequestDto registerRequest) {

        if (authService.register(registerRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * POST /login - Authentication endpoint.
     * 
     * @RequestBody - LoginRequestDto - object with user credentials.
     * @return ResponseEntity<Void> - 200 OK if authentication is successful, 401 Unauthorized if
     *         authentication fails.
     */
    @Operation(summary = "Авторизация пользователя")
    @Tag(name = "Авторизация")
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @NotNull @Valid @RequestBody LoginRequestDto login) {

        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
