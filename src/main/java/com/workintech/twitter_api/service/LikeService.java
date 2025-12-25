package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.entity.Like;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.UserRepository;
import com.workintech.twitter_api.repository.LikeRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository,
                       TweetRepository tweetRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    /**
     * toggleLike Metodu:
     * Twitter'daki gibi "kalbe basınca beğenme, tekrar basınca geri çekme" mantığıdır.
     */
    public boolean toggleLike(Long userId, Long tweetId) {

        // Veritabanını sorgula: "Bu kullanıcı bu tweeti daha önce beğenmiş mi?"
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(userId, tweetId);

        if (existingLike.isPresent()) {

            // DURUM A: Beğeni zaten var. O halde beğeniyi kaldırıyoruz (Unlike).
            likeRepository.delete(existingLike.get());
            return false;

        } else {

            // DURUM B: Beğeni yok. O halde yeni bir Like kaydı oluşturuyoruz.
            Tweet tweet = tweetRepository.findById(tweetId)
                    .orElseThrow(() -> new RuntimeException("Tweet bulunamadı"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            Like newLike = new Like();
            newLike.setTweet(tweet);
            newLike.setUser(user);
            likeRepository.save(newLike);

            return true;
        }
    }
}