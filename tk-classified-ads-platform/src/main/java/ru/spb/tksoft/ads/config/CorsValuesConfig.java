package ru.spb.tksoft.ads.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

/**
 * CORS: additional values.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsValuesConfig {

    private List<String> allowedOrigins;
}
