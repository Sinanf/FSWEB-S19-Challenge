package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.response.UserResponse;
import com.workintech.twitter_api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // Frontend'den erişim izni
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Arama Endpoint'i
    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String query) {
        return userRepository.searchUsers(query).stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getFirstName(), // YENİ
                        u.getLastName(),  // YENİ
                        u.getAvatar()     // YENİ
                ))
                .toList();
    }
}