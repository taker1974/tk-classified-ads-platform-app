package ru.spb.tksoft.ads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;

/**
 * Security config for Spring Security, basic authentication.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsValuesConfig values;

    /**
     * Configure Spring Security filters and authorization rules.
     * 
     * @param http HttpSecurity object.
     * @return SecurityFilterChain object.
     * 
     * @throws Exception If an error occurs.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/ads/**",
                                "/users/avatar/**",
                                "/ads/image/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/actuator/health")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(x -> {
                });

        return http.build();
    }

    /**
     * Configure CORS (Cross-Origin Resource Sharing) configuration source.
     * 
     * @return CorsConfigurationSource object.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        var config = new CorsConfiguration();

        // Enable CORS for this origins only because of security reasons:
        // basic authentication is used.
        config.setAllowedOrigins(values.getAllowedOrigins());

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Configure password encoder.
     * 
     * @return PasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
