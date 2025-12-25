package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.repository.UserRepository;
import com.workintech.twitter_api.service.TweetService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
@CrossOrigin(origins = "*") // Frontend (React) erişimi için şart
public class RetweetController {

    private final TweetService tweetService;
    private final UserRepository userRepository;

    public RetweetController(TweetService tweetService, UserRepository userRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
    }

    /**.
     * URL: POST http://localhost:8080/retweet/{originalTweetId}
     */
    @PostMapping("/{originalTweetId}")
    public Tweet retweet(@PathVariable Long originalTweetId, Authentication authentication) {
        // 1. O anki oturum açmış kullanıcıyı bul (Güvenlik)
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. Service katmanına "Bu kullanıcı, şu tweeti retweetledi" de.
        return tweetService.retweet(user.getId(), originalTweetId);
    }

    /**
     * Retweeti siler
     * URL: DELETE http://localhost:8080/retweet/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteRetweet(@PathVariable Long id) {
        tweetService.delete(id);
    }
}