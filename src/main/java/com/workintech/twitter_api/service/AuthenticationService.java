package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Şifreleri güvenli hale getirmek için.
    private final AuthenticationManager authenticationManager; // Giriş bilgilerini kontrol etmek için.

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // --- KAYIT OLMA (REGISTER) ---
    public User register(String firstName, String lastName, String email, String password) {
        // 1. Email kontrolü
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Bu email zaten kullanımda: " + email);
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        // BCrypt ile hashliyoruz
        user.setPassword(passwordEncoder.encode(password));

        // Email'in öncesini otomatik username yap
        user.setUsername(email.split("@")[0]);

        return userRepository.save(user);
    }

    // --- GİRİŞ YAPMA (LOGIN) ---
    public User login(String email, String password) {
        try {
            // Spring Security'nin Manager'ı email ve şifreyi otomatik doğrular.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            // Doğrulama başarılıysa kullanıcıyı döndür.
            return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        } catch (Exception e) {
            // Hatalı şifre veya email durumunda.
            throw new RuntimeException("Geçersiz kullanıcı adı veya şifre");
        }
    }
}