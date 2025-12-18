package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.LoginUser;
import com.workintech.twitter_api.dto.RegisterUser;
import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUser registerUser) {
        return authenticationService.register(
                registerUser.getUsername(),
                registerUser.getEmail(),
                registerUser.getPassword()
        );
    }

    // POST http://localhost:3000/auth/login
    @PostMapping("/login")
    public ApplicationUser login(@RequestBody LoginUser loginUser) {
        return authenticationService.login(
                loginUser.getEmail(),
                loginUser.getPassword()
        );
    }
}
