package com.workintech.twitter_api.service;

import com.workintech.twitter_api.dto.LikeRequest;
import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.entity.Like;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.repository.LikeRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ApplicationUserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, ApplicationUserRepository userRepository, TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    // BEĞENİ ATMA
    public Like addLike(LikeRequest likeRequest) {
        // 1. Kullanıcı ve Tweet var mı?
        ApplicationUser user = userRepository.findById(likeRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Tweet tweet = tweetRepository.findById(likeRequest.getTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));

        // 2. Daha önce beğenmiş mi? (Repository'e yazdığımız özel metot burada işe yarıyor)
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(likeRequest.getUserId(), likeRequest.getTweetId());

        if (existingLike.isPresent()) {
            throw new RuntimeException("This tweet is already liked by user!");
        }

        // 3. Beğeniyi kaydet
        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);

        return likeRepository.save(like);
    }

    // BEĞENİ GERİ ALMA (DISLIKE)
    public void removeLike(LikeRequest likeRequest) {
        // Beğeniyi bul
        Like existingLike = likeRepository.findByUserIdAndTweetId(likeRequest.getUserId(), likeRequest.getTweetId())
                .orElseThrow(() -> new RuntimeException("Like not found to remove!"));

        // Sil
        likeRepository.delete(existingLike);
    }
}