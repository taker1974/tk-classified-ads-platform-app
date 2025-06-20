package ru.spb.tksoft.ads.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Caffein cache config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableCaching
public class CaffeinCacheConfig {

    @Bean
    public CacheManager cacheManager() {

        var cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES));

        cacheManager.setAllowNullValues(false);

        return cacheManager;
    }
}
