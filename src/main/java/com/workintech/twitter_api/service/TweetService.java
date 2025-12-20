package com.workintech.twitter_api.service;

import com.workintech.twitter_api.dto.TweetResponse;
import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.repository.LikeRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tweet ve Retweet işlemlerinin ana iş mantığını yürüten servis sınıfı.
 */
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final ApplicationUserRepository userRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository,
                        ApplicationUserRepository userRepository,
                        LikeRepository likeRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    // ========================================================================
    // READ OPERATIONS (OKUMA İŞLEMLERİ)
    // ========================================================================

    /**
     * Ham tweet listesini tarihe göre sıralı döner.
     */
    public List<Tweet> findAll() {
        return tweetRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Belirli bir kullanıcının tweetlerini getirir.
     */
    public List<Tweet> findAllByUserId(Long userId) {
        return tweetRepository.findAllByUserId(userId);
    }

    /**
     * Frontend için zenginleştirilmiş tweet akışını (Feed) hazırlar.
     * Tweetlerin beğeni sayılarını ve mevcut kullanıcının beğenip beğenmediğini hesaplar.
     */
    public List<TweetResponse> findAllTweets() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();

        // 1. Giriş yapmış kullanıcıyı SecurityContext'ten bul
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = -1L;

        if (!email.equals("anonymousUser")) {
            currentUserId = userRepository.findByEmail(email).get().getId();
        }

        final Long finalUserId = currentUserId;

        // 2. Tweetleri zenginleştirilmiş Response DTO'larına dönüştür
        return tweets.stream()
                .map(tweet -> {
                    int likeCount = likeRepository.countByTweetId(tweet.getId());
                    boolean isLikedByMe = false;

                    if (finalUserId != -1L) {
                        isLikedByMe = likeRepository.findByUserIdAndTweetId(finalUserId, tweet.getId()).isPresent();
                    }

                    int retweetCount = 0; // İleride repository üzerinden hesaplanabilir

                    return new TweetResponse(
                            tweet.getId(),
                            tweet.getContent(),
                            tweet.getUser().getEmail(),
                            tweet.getUser().getUsername(),
                            likeCount,
                            retweetCount,
                            isLikedByMe,
                            tweet.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    // ========================================================================
    // WRITE OPERATIONS (YAZMA İŞLEMLERİ)
    // ========================================================================

    /**
     * Yeni bir tweet oluşturur.
     */
    public Tweet save(String content, Long userId) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);

        return tweetRepository.save(tweet);
    }

    /**
     * Var olan bir tweeti günceller.
     */
    public Tweet update(Long id, String newContent) {
        Tweet existingTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));

        existingTweet.setContent(newContent);
        return tweetRepository.save(existingTweet);
    }

    /**
     * Tweet silme işlemi.
     * Sadece tweetin sahibi olan kullanıcının silmesine izin verir.
     */
    public void delete(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));

        // Yetki Kontrolü
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ApplicationUser loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!tweet.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this tweet!");
        }

        tweetRepository.delete(tweet);
    }

    /**
     * Retweet mekanizması.
     * Orijinal tweetin içeriğini kopyalar ve 'retweetOf' alanı ile ilişkilendirir.
     */
    public Tweet retweet(Long userId, Long originalTweetId) {
        Tweet originalTweet = tweetRepository.findById(originalTweetId)
                .orElseThrow(() -> new RuntimeException("Original tweet not found!"));

        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Tweet retweet = new Tweet();
        retweet.setContent("RT: " + originalTweet.getContent());
        retweet.setUser(user);
        retweet.setRetweetOf(originalTweet);

        return tweetRepository.save(retweet);
    }
}