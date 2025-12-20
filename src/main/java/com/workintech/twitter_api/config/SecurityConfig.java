package com.workintech.twitter_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını kapat
                .authorizeHttpRequests(auth -> {
                    // Şimdilik ayrım yapmaksızın HERKESE (anyRequest) izin ver (permitAll)
                    auth.anyRequest().permitAll();
                })
                // Basic Auth'u da şimdilik devreden çıkarabiliriz veya default bırakabiliriz
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**", "/tweet/**", "/comment/**", "/like/**", "/dislike/**").permitAll();
                    auth.anyRequest().authenticated();
                })

                .httpBasic(Customizer.withDefaults())
                .build();
    }
    */
}
