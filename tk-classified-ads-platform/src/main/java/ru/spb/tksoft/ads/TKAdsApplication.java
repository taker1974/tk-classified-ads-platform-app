package ru.spb.tksoft.ads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TKAdsApplication {

  public static void main(String[] args) {

    SpringApplication.run(TKAdsApplication.class, args);
  }
}
