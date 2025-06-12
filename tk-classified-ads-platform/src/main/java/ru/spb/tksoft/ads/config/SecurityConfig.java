package ru.spb.tksoft.ads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.ads.dto.RegisterRequestDto;
import ru.spb.tksoft.ads.enumeration.UserRole;
import java.util.Arrays;
import java.util.List;

/**
 * Security config for Spring Security, basic authentication.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/register",
                                "/login")
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
        config.setAllowedOrigins(
                List.of("${advertising.recieve-from:http://localhost:3000}"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Configure in-memory user details service.
     * 
     * @return UserDetailsService object.
     */
    @Bean
    @SuppressWarnings("java:S1488") // Immediately return this expression instead of assigning it to
                                    // the temporary variable "manager".
    public UserDetailsService userDetailsService(@NotNull PasswordEncoder encoder) {

        // TODO: Temporary in-memory users (replace with database later)

        var manager = new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password("{noop}adminpass") // {noop} for plain
                                                     // text (remove in
                                                     // prod)
                        .passwordEncoder(encoder::encode)
                        .roles(UserRole.ADMIN.name())
                        .build(),
                User.builder()
                        .username("user")
                        .password("{noop}userpass")
                        .roles(UserRole.USER.name())
                        .build());
        return manager;
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
