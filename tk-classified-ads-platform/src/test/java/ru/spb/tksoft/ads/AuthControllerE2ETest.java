package ru.spb.tksoft.ads;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;

/**
 * E2E for AuthController.
 * 
 * https://www.baeldung.com/spring-boot-built-in-testcontainers
 * https://blog.jetbrains.com/idea/2024/12/testing-spring-boot-applications-using-testcontainers/
 * https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class AuthControllerE2ETest extends E2ETestBase {

    @BeforeEach
    void resetDatabase() {
        userRepository.deleteAll();
        userServiceCached.clearCaches();
    }

    @DisplayName("Register OK")
    @Test
    void register_201_whenValidRequest() {

        RegisterRequestDto request = createValidRegisterRequest();
        ResponseEntity<Void> response = sendRegisterRequest(request);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("Logged in OK")
    @Test
    void login_200_whenValidCredentials() {

        RegisterRequestDto request = createValidRegisterRequest();
        sendRegisterRequest(request);

        var loginRequest = new LoginRequestDto(
                request.getUsername(),
                request.getPassword());

        ResponseEntity<Void> response = sendLoginRequest(loginRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    static Stream<RegisterRequestDto> invalidRegisterRequests() {
        return Stream.of(
                new RegisterRequestDto("inv", "pwd", "John", "Doe", "+79991234567"),
                new RegisterRequestDto("a@b", "pwd", "John", "Doe", "+79991234567"),
                new RegisterRequestDto("valid@example.com", "", "John", "Doe", "+79991234567"),
                new RegisterRequestDto("valid@example.com", "short", "John", "Doe", "+79991234567"),
                new RegisterRequestDto("valid@example.com", "pass1111", "", "Doe", "+79991234567"),
                new RegisterRequestDto("valid@example.com", "pass1111", "J", "Doe", "+79991234567"),
                new RegisterRequestDto("valid@example.com", "pass1111", "John", "Doe", "phone"),
                new RegisterRequestDto(null, null, null, null, null, null));
    }

    @MethodSource("invalidRegisterRequests")
    @DisplayName("Register invalid")
    @ParameterizedTest
    void register_400_whenInvalidRequest(RegisterRequestDto invalidRequest) {

        ResponseEntity<Void> response = sendRegisterRequest(invalidRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    static Stream<LoginRequestDto> invalidLoginRequests() {
        return Stream.of(
                new LoginRequestDto("invalid", "password"),
                new LoginRequestDto("a@b", "password"),
                new LoginRequestDto("valid@example.com", ""),
                new LoginRequestDto("valid@example.com", "short"),
                new LoginRequestDto(null, null));
    }

    @MethodSource("invalidLoginRequests")
    @DisplayName("Login failed - 400")
    @ParameterizedTest
    void login_400_whenInvalidRequest(LoginRequestDto invalidRequest) {

        ResponseEntity<Void> response = sendLoginRequest(invalidRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Register failed - 400 when username exists")
    @Test
    void register_400_whenUsernameExists() {

        RegisterRequestDto firstUser = createValidRegisterRequest();
        sendRegisterRequest(firstUser);

        RegisterRequestDto secondUser = new RegisterRequestDto(
                "valid@example.com",
                "anotherPassword",
                "Second",
                "User",
                "+7(999)222-22-22");

        ResponseEntity<Void> response = sendRegisterRequest(secondUser);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Register failed - 400 when username exists")
    @Test
    void login_401_whenInvalidCredentials() {

        RegisterRequestDto request = createValidRegisterRequest();
        sendRegisterRequest(request);

        LoginRequestDto loginRequest = new LoginRequestDto(
                "valid@example.com",
                "wrongPassword");

        ResponseEntity<Void> response = sendLoginRequest(loginRequest);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
