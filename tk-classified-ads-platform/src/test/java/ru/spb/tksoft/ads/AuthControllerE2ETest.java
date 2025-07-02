package ru.spb.tksoft.ads;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.enumeration.UserRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void register_shouldReturn201_whenValidRequest() {
        // Arrange
        RegisterRequestDto request = createValidRegisterRequest();
        
        // Act
        ResponseEntity<Void> response = sendRegisterRequest(request);
        
        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void login_shouldReturn200_whenValidCredentials() {
        // Arrange
        RegisterRequestDto registerRequest = createValidRegisterRequest();
        sendRegisterRequest(registerRequest);
        
        LoginRequestDto loginRequest = new LoginRequestDto(
            "valid@example.com", 
            "validPassword123"
        );

        // Act
        ResponseEntity<Void> response = sendLoginRequest(loginRequest);
        
        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // // Параметризованные тесты для невалидных данных регистрации
    // static Stream<RegisterRequestDto> invalidRegisterRequests() {
    //     return Stream.of(
    //         new RegisterRequestDto("invalid", "pwd", "John", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("a@b", "pwd", "John", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("valid@example.com", "", "John", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("valid@example.com", "short", "John", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("valid@example.com", "password123", "", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("valid@example.com", "password123", "J", "Doe", "+79991234567", UserRole.USER),
    //         new RegisterRequestDto("valid@example.com", "password123", "John", "Doe", "invalid_phone", UserRole.USER),
    //         new RegisterRequestDto(null, null, null, null, null, null)
    //     );
    // }

    // @ParameterizedTest
    // @MethodSource("invalidRegisterRequests")
    // void register_shouldReturn400_whenInvalidRequest(RegisterRequestDto invalidRequest) {
    //     // Act
    //     ResponseEntity<Void> response = sendRegisterRequest(invalidRequest);
        
    //     // Assert
    //     Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // }

    // // Параметризованные тесты для невалидных данных аутентификации
    // static Stream<LoginRequestDto> invalidLoginRequests() {
    //     return Stream.of(
    //         new LoginRequestDto("invalid", "password"),
    //         new LoginRequestDto("a@b", "password"),
    //         new LoginRequestDto("valid@example.com", ""),
    //         new LoginRequestDto("valid@example.com", "short"),
    //         new LoginRequestDto(null, null)
    //     );
    // }

    // @ParameterizedTest
    // @MethodSource("invalidLoginRequests")
    // void login_shouldReturn400_whenInvalidRequest(LoginRequestDto invalidRequest) {
    //     // Act
    //     ResponseEntity<Void> response = sendLoginRequest(invalidRequest);
        
    //     // Assert
    //     Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // }

    // @Test
    // void register_shouldReturn400_whenUsernameExists() {
    //     // Arrange
    //     RegisterRequestDto firstUser = createValidRegisterRequest();
    //     sendRegisterRequest(firstUser);
        
    //     RegisterRequestDto secondUser = new RegisterRequestDto(
    //         "valid@example.com",
    //         "anotherPassword",
    //         "Second",
    //         "User",
    //         "+7(999)222-22-22",
    //         UserRole.USER
    //     );

    //     // Act
    //     ResponseEntity<Void> response = sendRegisterRequest(secondUser);
        
    //     // Assert
    //     Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // }

    // @Test
    // void login_shouldReturn401_whenInvalidCredentials() {
    //     // Arrange
    //     RegisterRequestDto registerRequest = createValidRegisterRequest();
    //     sendRegisterRequest(registerRequest);
        
    //     LoginRequestDto loginRequest = new LoginRequestDto(
    //         "valid@example.com", 
    //         "wrongPassword"
    //     );

    //     // Act
    //     ResponseEntity<Void> response = sendLoginRequest(loginRequest);
        
    //     // Assert
    //     Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    // }

    private RegisterRequestDto createValidRegisterRequest() {

        return new RegisterRequestDto(
            "valid@example.com",
            "validPassword123",
            "Johnadfsdfs",
            "DoeAdsds",
            "+79991234567",
            UserRole.USER
        );
    }

    private ResponseEntity<Void> sendRegisterRequest(RegisterRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(baseUrl + "/register", entity, Void.class);
    }

    private ResponseEntity<Void> sendLoginRequest(LoginRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(baseUrl + "/login", entity, Void.class);
    }
}