package ru.spb.tksoft.ads;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerE2ETest extends E2ETestBase {

    private final RegisterRequestDto goodRegisterRequestDto = createValidRegisterRequest();

    private ResponseEntity<Void> registerOk() {
        RegisterRequestDto request = createValidRegisterRequest();
        return sendRegisterRequest(request);
    }

    @DisplayName("Registered - 201, otherwise - 400 or whatever")
    @Test
    void register_201_whenValidRequest() {
        Assertions.assertEquals(HttpStatus.CREATED, registerOk().getStatusCode());
    }

    @DisplayName("Logged in - 200, otherwise - 400 or whatever")
    @Test
    void login_200_whenValidCredentials() {

        registerOk();

        var loginRequest = new LoginRequestDto(
                goodRegisterRequestDto.getUsername(),
                goodRegisterRequestDto.getPassword());

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

    @DisplayName("Register failed - 400")
    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    void register_400_whenInvalidRequest(RegisterRequestDto invalidRequest) {

        ResponseEntity<Void> response = sendRegisterRequest(invalidRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // // Параметризованные тесты для невалидных данных аутентификации
    // static Stream<LoginRequestDto> invalidLoginRequests() {
    // return Stream.of(
    // new LoginRequestDto("invalid", "password"),
    // new LoginRequestDto("a@b", "password"),
    // new LoginRequestDto("valid@example.com", ""),
    // new LoginRequestDto("valid@example.com", "short"),
    // new LoginRequestDto(null, null)
    // );
    // }

    // @ParameterizedTest
    // @MethodSource("invalidLoginRequests")
    // void login_shouldReturn400_whenInvalidRequest(LoginRequestDto invalidRequest) {
    // // Act
    // ResponseEntity<Void> response = sendLoginRequest(invalidRequest);

    // // Assert
    // Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // }

    // @Test
    // void register_shouldReturn400_whenUsernameExists() {
    // // Arrange
    // RegisterRequestDto firstUser = createValidRegisterRequest();
    // sendRegisterRequest(firstUser);

    // RegisterRequestDto secondUser = new RegisterRequestDto(
    // "valid@example.com",
    // "anotherPassword",
    // "Second",
    // "User",
    // "+7(999)222-22-22",
    // UserRole.USER
    // );

    // // Act
    // ResponseEntity<Void> response = sendRegisterRequest(secondUser);

    // // Assert
    // Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // }

    // @Test
    // void login_shouldReturn401_whenInvalidCredentials() {
    // // Arrange
    // RegisterRequestDto registerRequest = createValidRegisterRequest();
    // sendRegisterRequest(registerRequest);

    // LoginRequestDto loginRequest = new LoginRequestDto(
    // "valid@example.com",
    // "wrongPassword"
    // );

    // // Act
    // ResponseEntity<Void> response = sendLoginRequest(loginRequest);

    // // Assert
    // Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    // }
}
