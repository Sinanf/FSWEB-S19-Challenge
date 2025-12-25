package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.request.LoginRequest;
import com.workintech.twitter_api.dto.request.RegisterRequest;
import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

// @RestController: Bu sınıfın bir web denetleyicisi olduğunu ve JSON döneceğini belirtir.
@RestController
@RequestMapping("/auth") // Tüm adresler localhost:8080/auth ile başlar.
@CrossOrigin(origins = "*") // Frontend (React) farklı portta olduğu için erişim izni verdik.
public class AuthController {

    private final AuthenticationService authService;

    // Dependency Injection: İş mantığını yürüten servisi buraya bağlıyoruz.
    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    // Kayıt olma isteğini karşılar (POST)
    // @RequestBody: Gelen JSON verisini (DTO) Java nesnesine dönüştürür.
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        // Gelen paketi açıp servise işlemesi için gönderiyoruz.
        return authService.register(req.firstName(), req.lastName(), req.email(), req.password());
    }

    // Giriş yapma isteğini karşılar
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest req) {
        return authService.login(req.email(), req.password());
    }
}