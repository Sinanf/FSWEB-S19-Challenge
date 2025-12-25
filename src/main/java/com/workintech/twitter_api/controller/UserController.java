package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.response.UserResponse;
import com.workintech.twitter_api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- KULLANICI ARAMA (SEARCH) ---

    /**
     * URL: GET http://localhost:8080/user/search?query=ahmet
     */
    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String query) {
        // Stream API: Veritabanından gelen User listesini dönüştürüyoruz.
        // Amaç: Şifre gibi gizli verileri Client'a yollamamak (DTO Dönüşümü).
        return userRepository.searchUsers(query).stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getAvatar()
                ))
                .toList();
    }
}