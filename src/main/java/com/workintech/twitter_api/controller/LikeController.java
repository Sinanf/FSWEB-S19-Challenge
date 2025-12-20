package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public LikeController(LikeService likeService, ApplicationUserRepository userRepository) {
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    // ========================================================================
    // LIKE OPERATIONS (BEĞENME İŞLEMLERİ)
    // ========================================================================

    /**
     * Bir tweeti beğenir veya beğeniyi geri çeker (Toggle mantığı).
     * URL: POST http://localhost:8080/like/{tweetId}
     */
    @PostMapping("/{tweetId}")
    public boolean likeTweet(@PathVariable Long tweetId) {
        // 1. Giriş yapan kullanıcının emailini al
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Emailden kullanıcıyı bul (Yoksa hata fırlat)
        ApplicationUser user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 3. Gerçek kullanıcının ID'si ile beğeni işlemini yap
        return likeService.toggleLike(user.getId(), tweetId);
    }
}