package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Kullanıcı kimlik doğrulama işlemlerini (Kayıt ve Giriş) yöneten servis.
 * Şifrelerin güvenli bir şekilde saklanması ve kontrol edilmesinden sorumludur.
 */
@Service
public class AuthenticationService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(ApplicationUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================================================================
    // REGISTER (KAYIT OL)
    // ========================================================================

    /**
     * Yeni bir kullanıcıyı doğrular, şifresini hash'ler ve veritabanına kaydeder.
     */
    public ApplicationUser register(String username, String email, String password) {
        // 1. Email Çakışma Kontrolü: Bu email daha önce alınmış mı?
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with given email already exists");
        }

        // 2. Yeni Kullanıcı Nesnesini Hazırla
        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername(username);
        newUser.setEmail(email);

        // 3. Güvenlik: Ham şifreyi (raw password) BCrypt ile hash'leyerek kaydet
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        // 4. Veritabanına Yaz ve Sonucu Dön
        return userRepository.save(newUser);
    }

    // ========================================================================
    // LOGIN (GİRİŞ YAP)
    // ========================================================================

    /**
     * Kullanıcının girdiği bilgileri kontrol eder.
     * Başarılı ise kullanıcı bilgilerini döner, başarısız ise hata fırlatır.
     */
    public ApplicationUser login(String email, String password) {
        // 1. Kullanıcıyı Email ile Sorgula
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Şifre Doğrulama: Gelen şifre ile veritabanındaki hash'li şifre eşleşiyor mu?
        // passwordEncoder.matches(ham_sifre, hashlenmis_sifre)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Kimlik Doğrulama Başarılı
        return user;
    }
}