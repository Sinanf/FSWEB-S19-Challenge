package com.workintech.twitter_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    // Şifreleri veritabanında düz metin (plain text) saklamamak için BCrypt ile şifreliyoruz.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Kullanıcı doğrulama yöneticisi. DB'deki kullanıcı ile girilen şifreyi kıyaslar.
    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    // Ana güvenlik filtresi
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // REST API olduğu için CSRF korumasına gerek yok.
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // React ile iletişim için CORS ayarını bağladık.
                .authorizeHttpRequests(auth -> {
                    // Tarayıcı ön uçuş (Pre-flight) isteklerine izin ver.
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    // Login ve Register endpointleri herkese açık olmalı.
                    auth.requestMatchers("/auth/**").permitAll();
                    // Diğer tüm istekler için giriş yapılmış olması şart.
                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults()) // Basic Auth kullanıyoruz.
                .build();
    }

    // CORS Ayarları: Frontend (React) farklı portta olduğu için izin vermemiz şart.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*")); // Her yerden gelen isteği kabul et (Localhost vs.)
        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // İzin verilen HTTP metodları
        cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}