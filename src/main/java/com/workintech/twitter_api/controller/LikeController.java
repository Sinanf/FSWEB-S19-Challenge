package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.repository.UserRepository;
import com.workintech.twitter_api.service.LikeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@CrossOrigin(origins = "*") // Frontend erişimi için
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;

    public LikeController(LikeService likeService, UserRepository userRepository) {
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    /**
     * Beğenme / Beğeniden Vazgeçme (Toggle)
     * POST http://localhost:8080/like/{tweetId}
     */
    @PostMapping("/{tweetId}")
    public boolean likeTweet(@PathVariable Long tweetId) {
        // 1. Güvenlik katmanından (Context) giriş yapan kullanıcıyı bul
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Veritabanından kullanıcı ID'sini çek
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 3. Service'e gönder: Eğer beğenmişse siler, beğenmemişse ekler (Toggle)
        return likeService.toggleLike(user.getId(), tweetId);
    }
}