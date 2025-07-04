package ru.spb.tksoft.ads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.spb.tksoft.ads.dto.request.LoginRequestDto;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.enumeration.UserRole;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.ads.service.UserServiceCached;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class E2ETestBase {

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserServiceCached userServiceCached;

    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5"))
                    .withDatabaseName("tk_ads")
                    .withUsername("ads_god")
                    .withPassword("87654321")
                    .withExposedPorts(5432)
                    .withReuse(true);

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
                "validPassword123",
                "John",
                "Doe",
                "+79991234567",
                UserRole.USER);
    }

    protected ResponseEntity<Void> sendRegisterRequest(RegisterRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(getBaseUrl() + "/register", entity, Void.class);
    }

    protected ResponseEntity<Void> sendLoginRequest(LoginRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(getBaseUrl() + "/login", entity, Void.class);
    }
}
