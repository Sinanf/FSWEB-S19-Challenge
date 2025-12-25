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
    private final CommentRepository commentRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository, UserRepository userRepository,
                        LikeRepository likeRepository, CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    // --- YARDIMCI METOD: Tweet -> TweetResponse Dönüşümü ---
    private TweetResponse convertToResponse(Tweet tweet, Long finalUserId) {
        int likeCount = likeRepository.countByTweetId(tweet.getId());
        int retweetCount = tweetRepository.countByRetweetOfId(tweet.getId());
        int commentCount = commentRepository.countByTweetId(tweet.getId());


        boolean isLikedByMe = (finalUserId != -1L) &&
                likeRepository.findByUserIdAndTweetId(finalUserId, tweet.getId()).isPresent();

        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getEmail(),
                tweet.getUser().getFirstName(),
                tweet.getUser().getLastName(),
                tweet.getUser().getAvatar(),
                likeCount,
                retweetCount,
                commentCount,
                isLikedByMe,
                tweet.getCreatedAt()
        );
    }

    public List<TweetResponse> findAllTweets() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
        Long finalUserId = getCurrentUserId();

        return tweets.stream()
                .map(tweet -> convertToResponse(tweet, finalUserId))
                .collect(Collectors.toList());
    }

    public List<TweetResponse> findAllTweetsByUserId(Long userId) {
        List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        Long finalUserId = getCurrentUserId();

        return tweets.stream()
                .map(tweet -> convertToResponse(tweet, finalUserId))
                .collect(Collectors.toList());
    }

    // --- GÜVENLİK: Giriş yapan kullanıcının ID'sini çekme ---
    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) return -1L;
        return userRepository.findByEmail(email).map(User::getId).orElse(-1L);
    }

    // --- YAZMA İŞLEMLERİ  ---
    public Tweet save(String content, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);
        return tweetRepository.save(tweet);
    }

    public void delete(Long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(() -> new RuntimeException("Tweet not found!"));
        tweetRepository.delete(tweet);
    }

    public Tweet update(Long id, String newContent) {
        Tweet existingTweet = tweetRepository.findById(id).orElseThrow(() -> new RuntimeException("Tweet not found!"));
        existingTweet.setContent(newContent);
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
}