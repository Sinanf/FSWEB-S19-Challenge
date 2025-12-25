package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.request.LoginRequest;
import com.workintech.twitter_api.dto.request.RegisterRequest;
import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        return authService.register(req.firstName(), req.lastName(), req.email(), req.password());
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest req) {
        return authService.login(req.email(), req.password());
    }
}