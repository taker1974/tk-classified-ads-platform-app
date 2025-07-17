package ru.spb.tksoft.ads;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.enumeration.UserRole;
import ru.spb.tksoft.ads.repository.AdRepository;
import ru.spb.tksoft.ads.repository.CommentRepository;
import ru.spb.tksoft.ads.repository.ImageRepository;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.ads.service.AdServiceCached;
import ru.spb.tksoft.ads.service.UserServiceCached;

/**
 * Base class for E2E tests with testcontainers.
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
abstract class E2ETestBase {

    protected static final String TEST_IMAGE = "test-avatar.jpg";
    protected byte[] testImageBytes;

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AdRepository adRepository;

    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected UserServiceCached userServiceCached;

    @Autowired
    protected AdServiceCached adServiceCached;

    protected String api() {
        return "http://localhost:" + port;
    }

    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres =

            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5"))
                    .withDatabaseName("tk_ads")
                    .withUsername("ads_god")
                    .withPassword("87654321")
                    .withExposedPorts(5432)
                    .withReuse(true)
                    .withLabel("reuse.UUID", "e06307ab-8fad-55e4-8f9d-0e5470ac7456");

    @DynamicPropertySource
    protected static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.ads.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.ads.username", postgres::getUsername);
        registry.add("spring.datasource.ads.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    protected RegisterRequestDto createValidRegisterRequest() {

        return new RegisterRequestDto(
                "valid@example.com",
                "valid678",
                "John",
                "Doe",
                "+79991234567",
                UserRole.USER);
    }

    protected ResponseEntity<Void> sendRegisterRequest(RegisterRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(api() + "/register", entity, Void.class);
    }

    protected ResponseEntity<Void> sendLoginRequest(LoginRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(api() + "/login", entity, Void.class);
    }
    protected static record UserCredentials(String name, String password) {

        /** Base64 from name and password. */
        public String getAuthEncodedString() {
            String auth = name + ":" + password;
            return Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected HttpHeaders createBasicAuthHeaders(UserCredentials credentials) {

        var headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + credentials.getAuthEncodedString());
        return headers;
    }

    protected UserCredentials registerAndLoginUser() {

        RegisterRequestDto registerRequest = createValidRegisterRequest();
        ResponseEntity<Void> registerResponse = sendRegisterRequest(registerRequest);
        Assertions.assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());

        LoginRequestDto loginRequest = new LoginRequestDto(
                registerRequest.getUsername(),
                registerRequest.getPassword());
        ResponseEntity<Void> loginResponse = restTemplate.postForEntity(
                api() + "/login", loginRequest, Void.class);
        Assertions.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        return new UserCredentials(
                registerRequest.getUsername(),
                registerRequest.getPassword());
    }

    protected AdResponseDto createAd(UserCredentials credentials) {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        CreateOrUpdateAdRequestDto properties = new CreateOrUpdateAdRequestDto(
                "Test Ad",
                1000,
                "Test description");

        ByteArrayResource resource = new ByteArrayResource(testImageBytes) {
            @Override
            public String getFilename() {
                return TEST_IMAGE;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", resource);

        ResponseEntity<AdResponseDto> response = restTemplate.exchange(
                api() + "/ads",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                AdResponseDto.class);

        return response.getBody();
    }
}
