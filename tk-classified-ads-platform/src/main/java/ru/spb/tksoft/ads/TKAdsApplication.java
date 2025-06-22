package ru.spb.tksoft.ads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.spb.tksoft.ads.tools.ImageValidationProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(ImageValidationProperties.class)
public class TKAdsApplication {

  public static void main(String[] args) {

    SpringApplication.run(TKAdsApplication.class, args);
  }
}
