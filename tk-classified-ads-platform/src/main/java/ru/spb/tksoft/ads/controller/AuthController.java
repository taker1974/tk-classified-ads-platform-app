package ru.spb.tksoft.ads.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.spb.tksoft.ads.dto.LoginRequestDto;
import ru.spb.tksoft.ads.dto.RegisterRequestDto;
import ru.spb.tksoft.ads.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;

@Slf4j
@CrossOrigin(value = "${advertising.recieve-from:http://localhost:3000}")
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
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDto register) {

        if (authService.register(register)) {
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
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto login) {

        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
