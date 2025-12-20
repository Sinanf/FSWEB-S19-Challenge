package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.LoginUser;
import com.workintech.twitter_api.dto.RegisterUser;
import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationService authenticationService, ApplicationUserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    // ========================================================================
    // PUBLIC ENDPOINTS (HERKESE AÇIK)
    // ========================================================================

    /**
     * Yeni kullanıcı kaydı oluşturur.
     * URL: POST http://localhost:8080/auth/register
     */
    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUser registerUser) {
        return authenticationService.register(
                registerUser.getUsername(),
                registerUser.getEmail(),
                registerUser.getPassword()
        );
    }

    /**
     * Kullanıcı girişi yapar ve kullanıcı bilgilerini döner.
     * URL: POST http://localhost:8080/auth/login
     */
    @PostMapping("/login")
    public ApplicationUser login(@RequestBody LoginUser loginUser) {
        return authenticationService.login(
                loginUser.getEmail(),
                loginUser.getPassword()
        );
    }

    // ========================================================================
    // PROTECTED ENDPOINTS (GİRİŞ YAPILMIŞ OLMALI)
    // ========================================================================

    /**
     * O anki oturum açmış kullanıcının bilgilerini getirir.
     * Frontend, token veya Basic Auth ile istek attığında "Ben kimim?" sorusunu bu endpoint ile sorar.
     * URL: GET http://localhost:8080/auth/me
     */
    @GetMapping("/me")
    public ApplicationUser me(Authentication authentication) {
        // Security Context'teki email bilgisinden (authentication.getName()) kullanıcıyı bulur
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}