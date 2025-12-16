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
}
