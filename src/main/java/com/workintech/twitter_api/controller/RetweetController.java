package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final TweetService tweetService;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public RetweetController(TweetService tweetService, ApplicationUserRepository userRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
    }

    // ========================================================================
    // RETWEET OPERATIONS (RETWEET ETME / GERİ ALMA)
    // ========================================================================

    /**
     * Bir tweeti Retweet yapar.
     * URL: POST http://localhost:8080/retweet/{originalTweetId}
     */
    @PostMapping("/{originalTweetId}")
    public Tweet retweet(@PathVariable Long originalTweetId, Authentication authentication) {
        // 1. Giriş yapan kullanıcıyı SecurityContext (Authentication) üzerinden bul
        Long userId = userRepository.findByEmail(authentication.getName()).get().getId();

        // 2. Service katmanındaki retweet metodunu çağır
        return tweetService.retweet(userId, originalTweetId);
    }

    /**
     * Yapılan bir Retweet'i siler
     * Retweet teknik olarak bir Tweet olduğu için standart silme işlemi uygulanır.
     * URL: DELETE http://localhost:8080/retweet/{id}
     */
    @DeleteMapping("/{id}")
    public String deleteRetweet(@PathVariable Long id) {
        tweetService.delete(id);
        return "Retweet deleted successfully";
    }
}