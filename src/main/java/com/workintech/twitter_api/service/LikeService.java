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

/**
 * Tweet beğenme ve beğeniyi geri çekme işlemlerini yöneten servis.
 */
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
     * Beğeni İşlemi (Toggle Mantığı):
     */
    public boolean toggleLike(Long userId, Long tweetId) {

        // 1. Veritabanında bu kullanıcının bu tweet için bir beğenisi var mı kontrol et
        Optional<Like> existingLike = likeRepository.findByUserIdAndTweetId(userId, tweetId);

        if (existingLike.isPresent()) {
            // DURUM A: Beğeni mevcut -> Sil (Unlike)
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            // DURUM B: Beğeni yok -> Yeni Beğeni Oluştur (Like)

            // İlgili tweet ve kullanıcıyı doğrula
            Tweet tweet = tweetRepository.findById(tweetId)
                    .orElseThrow(() -> new RuntimeException("Tweet bulunamadı"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User bulunamadı"));

            // İlişkileri kur ve kaydet
            Like newLike = new Like();
            newLike.setTweet(tweet);
            newLike.setUser(user);
            likeRepository.save(newLike);

            return true;
        }
    }
}