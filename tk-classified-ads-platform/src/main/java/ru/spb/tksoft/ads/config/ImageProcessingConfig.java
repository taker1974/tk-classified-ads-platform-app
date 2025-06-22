package ru.spb.tksoft.ads.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import lombok.RequiredArgsConstructor;

/**
 * Image limits config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@RequiredArgsConstructor
public class ImageProcessingConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ImageProcessingProperties avatarImageProcessing() {
        return Binder.get(environment)
                .bind("image.avatar", ImageProcessingProperties.class)
                .orElseThrow(() -> new IllegalStateException("Error binding image.avatar"));
    }

    @Bean
    public ImageProcessingProperties adImageProcessing() {
        return Binder.get(environment)
                .bind("image.ad", ImageProcessingProperties.class)
                .orElseThrow(() -> new IllegalStateException("Error binding image.ad"));
    }

}
