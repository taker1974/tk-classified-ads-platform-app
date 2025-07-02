package ru.spb.tksoft.ads;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("Java(536871799)") // Resource leak: '<unassigned Closeable value>' is never closed Java(536871799)
public abstract class TestcontainersBase {

    // Actually it's not a leak.
    @Container
    public static final PostgreSQLContainer<?> postgres =

            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5"))
                    .withDatabaseName("tk_ads")
                    .withUsername("ads_god")
                    .withPassword("87654321")
                    .withExposedPorts(5432)
                    .withReuse(true);

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // Для Flyway/Liquibase (если используете)
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    }
}
