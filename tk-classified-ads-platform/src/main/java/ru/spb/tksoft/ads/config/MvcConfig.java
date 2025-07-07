package ru.spb.tksoft.ads.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Resource configuration.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/users/avatar/**", "/ads/image/**")
                .addResourceLocations("${image.avatar.storage-path}", "${image.ad.storage-path}")
                .setCachePeriod(3600); // Caching for 1 hour
    }
}
