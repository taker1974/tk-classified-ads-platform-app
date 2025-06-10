package ru.spb.tksoft.ads.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * DB JPA config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {
        "ru.spb.tksoft.ads.entity"})
@EnableJpaRepositories(
        entityManagerFactoryRef = "adsEntityManagerFactory",
        transactionManagerRef = "adsTransactionManager",
        basePackages = {
                "ru.spb.tksoft.ads.repository"})
@RequiredArgsConstructor
public class AdsDatabaseConfig {

    @NotNull
    private final Environment environment;

    /**
     * Create fabric of entities and sessions for ads.
     * 
     * @param builder Instance of EntityManagerFactoryBuilder.
     * @param dataSource DataSource for ads.
     * @return Instance of entity manager factory.
     */
    @Bean(name = "adsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("adsDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect",
                environment.getProperty("spring.jpa.database-platform"));

        return builder.dataSource(dataSource)
                .packages(
                        "ru.spb.tksoft.ads.entity")
                .properties(properties).build();
    }

    /**
     * Create transaction manager for ads.
     * 
     * @param entityManagerFactory Fabric of entities and sessions for ads.
     * @return Instance of transaction manager for adsing.
     */
    @Bean(name = "adsTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("adsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
