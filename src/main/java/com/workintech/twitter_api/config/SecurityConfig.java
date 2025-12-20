package com.workintech.twitter_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    // ========================================================================
    // TEMEL GÜVENLİK BİLEŞENLERİ (PASSWORD ENCODER & AUTH MANAGER)
    // ========================================================================

    /**
     * Şifreleme algoritmasını belirler (BCrypt).
     * Veritabanındaki hash'lenmiş şifrelerin doğrulanması için kullanılır.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Kimlik doğrulama yöneticisini yapılandırır.
     * Kullanıcı verilerini veritabanından (UserDetailsService) okuyarak doğrular.
     */
    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    // ========================================================================
    // GÜVENLİK FİLTRESİ ZİNCİRİ (SECURITY FILTER CHAIN)
    // ========================================================================

    /**
     * HTTP isteklerinin güvenlik kurallarını belirler.
     * Hangi adreslerin açık (public), hangilerinin kilitli (authenticated) olduğunu tanımlar.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF korumasını kapatıyoruz (Rest API'lerde genelde kapalı olur)
                .csrf(csrf -> csrf.disable())

                // CORS ayarlarını aşağıdaki corsConfigurationSource metoduna bağlıyoruz
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // İsteklerin yetki kontrolü
                .authorizeHttpRequests(auth -> {
                    // 1. Tarayıcıların gönderdiği ön kontrol (Pre-flight) isteklerine izin ver
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    // 2. Auth endpointleri (Login/Register) herkese açık olmalı
                    auth.requestMatchers("/auth/**").permitAll();

                    // 3. Diğer tüm istekler için giriş yapılmış olması zorunlu
                    auth.anyRequest().authenticated();
                })

                // HTTP Basic Auth (Basit kullanıcı adı/şifre girişi) kullan
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    // ========================================================================
    // CORS AYARLARI (FRONTEND İLETİŞİMİ)
    // ========================================================================

    /**
     * CORS (Cross-Origin Resource Sharing) Yapılandırması.
     * Farklı portta veya domainde çalışan Frontend'in, Backend'e istek atabilmesine izin verir.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Hangi kaynaklardan istek gelebilir? ("*" = Her yerden)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // Hangi HTTP metodlarına izin var?
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Hangi başlıklar (Header) gönderilebilir?
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Çerez (Cookie) ve kimlik bilgilerine izin ver
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Bu ayarları tüm endpointler (/**) için uygula
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}