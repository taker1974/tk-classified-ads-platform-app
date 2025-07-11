package ru.spb.tksoft.ads.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine cache config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    public static final long ENTRY_TTL = 10;

    @Bean
    public CacheManager caffeineCacheManager() {

        var cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(ENTRY_TTL, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }
}
