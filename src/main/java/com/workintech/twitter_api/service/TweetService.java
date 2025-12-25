package com.workintech.twitter_api.service;

import com.workintech.twitter_api.dto.response.TweetResponse;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.repository.CommentRepository;
import com.workintech.twitter_api.repository.LikeRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import com.workintech.twitter_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository; // Eksikti, eklendi

    @Autowired
    public TweetService(TweetRepository tweetRepository, UserRepository userRepository,
                        LikeRepository likeRepository, CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public List<TweetResponse> findAllTweets() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Long currentUserId = -1L;
        if (!email.equals("anonymousUser")) {
            currentUserId = userRepository.findByEmail(email).map(User::getId).orElse(-1L);
        }
        final Long finalUserId = currentUserId;

        return tweets.stream().map(tweet -> {
            int likeCount = likeRepository.countByTweetId(tweet.getId());
            int retweetCount = tweetRepository.countByRetweetOfId(tweet.getId());
            int commentCount = commentRepository.countByTweetId(tweet.getId()); // Düzeltildi
            boolean isLikedByMe = (finalUserId != -1L) && likeRepository.findByUserIdAndTweetId(finalUserId, tweet.getId()).isPresent();

            return new TweetResponse(
                    tweet.getId(),
                    tweet.getContent(),
                    tweet.getUser().getEmail(),
                    tweet.getUser().getUsername(),
                    likeCount,
                    retweetCount,
                    commentCount,
                    isLikedByMe,
                    tweet.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }

    public Tweet save(String content, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);
        return tweetRepository.save(tweet);
    }

    // Retweet, Delete ve diğer metodlar aynen kalacak (yorumları silinmiş haliyle)...
    // (Yer kaplamasın diye hepsini yazmadım, mevcut mantığın doğru)
    public void delete(Long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(() -> new RuntimeException("Tweet not found!"));
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail).orElseThrow(() -> new RuntimeException("User not found!"));

        if (!tweet.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Unauthorized delete!");
        }
        tweetRepository.delete(tweet);
    }

    /**
     * Var olan bir tweeti günceller.
     * Controller'dan gelen update isteği burayı çağırır.
     */
    public Tweet update(Long id, String newContent) {
        // 1. Tweet var mı diye bak
        Tweet existingTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));

        // 2. İçeriği güncelle
        existingTweet.setContent(newContent);

        // 3. Kaydet ve döndür
        return tweetRepository.save(existingTweet);
    }

    public Tweet retweet(Long userId, Long originalTweetId) {
        Tweet original = tweetRepository.findById(originalTweetId).orElseThrow(() -> new RuntimeException("Original tweet not found!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Tweet rt = new Tweet();
        rt.setContent("RT: " + original.getContent());
        rt.setUser(user);
        rt.setRetweetOf(original);
        return tweetRepository.save(rt);
    }

    public List<TweetResponse> findAllTweetsByUserId(Long userId) {
        // 1. Kullanıcının tweetlerini DB'den çek (Tarihe göre sıralı)
        List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        // Eğer repository'de 'OrderByCreatedAtDesc' yoksa sadece 'findAllByUserId' kullan,
        // sonra burada .stream().sorted(...) yapabilirsin.
        // Ama Repository'e eklemek daha performanslıdır.

        // Giriş yapan kullanıcıyı bul (Beğeni kontrolü için)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = -1L;
        if (!email.equals("anonymousUser")) {
            currentUserId = userRepository.findByEmail(email).map(User::getId).orElse(-1L);
        }
        final Long finalUserId = currentUserId;

        // 2. DTO'ya çevir
        return tweets.stream().map(tweet -> {
            int likeCount = likeRepository.countByTweetId(tweet.getId());
            int retweetCount = tweetRepository.countByRetweetOfId(tweet.getId());
            int commentCount = commentRepository.countByTweetId(tweet.getId());
            boolean isLikedByMe = (finalUserId != -1L) && likeRepository.findByUserIdAndTweetId(finalUserId, tweet.getId()).isPresent();

            return new TweetResponse(
                    tweet.getId(),
                    tweet.getContent(),
                    tweet.getUser().getEmail(),
                    tweet.getUser().getUsername(),
                    likeCount,
                    retweetCount,
                    commentCount,
                    isLikedByMe,
                    tweet.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }
}