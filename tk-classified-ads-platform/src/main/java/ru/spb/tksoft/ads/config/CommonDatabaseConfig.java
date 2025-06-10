package ru.spb.tksoft.ads.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Common DB config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
public class CommonDatabaseConfig {

    /**
     * Default constructor.
     */
    public CommonDatabaseConfig() {
        // ...
    }

    /**
     * Main datasource.
     * 
     * @return Main datasource.
     */
    @Bean(name = "adsDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.ads")
    public DataSource adsDataSource() {
        return DataSourceBuilder.create().build();
    }
}
