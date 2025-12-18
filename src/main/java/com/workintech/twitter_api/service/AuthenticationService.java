package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(ApplicationUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser register(String username, String email, String password) {
        // 1. Önce bu email ile kayıtlı biri var mı kontrol et
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with given email already exists");
        }

        // 2. Yeni kullanıcı oluştur
        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername(username);
        newUser.setEmail(email);

        // 3. Şifreyi hashleyerek (şifreleyerek) kaydet
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        // 4. Veritabanına yaz
        return userRepository.save(newUser);
    }

    public ApplicationUser login(String email, String password) {
        // 1. Email ile kullanıcıyı bul
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Şifreler uyuşuyor mu kontrol et
        // passwordEncoder.matches(ham_sifre, veritabanindaki_sifreli_hal)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Her şey doğruysa kullanıcıyı dön
        return user;
    }

}
