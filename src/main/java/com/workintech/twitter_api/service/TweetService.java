package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository, ApplicationUserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public List<Tweet> findAll() {
        return tweetRepository.findAllByOrderByCreatedAtDesc();
    }


    public List<Tweet> findAllByUserId(Long userId) {
        return tweetRepository.findAllByUserId(userId);
    }

    public Tweet save(String content, Long userId) {

        // 1. Önce kullanıcı veritabanında var mı diye bakıyoruz
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 2. Tweet nesnesini oluşturuyoruz
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user); // İlişkiyi kuruyoruz (Bu tweet bu user'ındır)

        // 3. Kaydediyoruz
        return tweetRepository.save(tweet);

    }

    // Tweet Silme
    public void delete(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));
        tweetRepository.delete(tweet);
    }

    // Tweet Güncelleme
    public Tweet update(Long id, String newContent) {
        Tweet existingTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found!"));

        existingTweet.setContent(newContent);
        // updated_at gibi bir alanın varsa onu da güncelleyebilirsin

        return tweetRepository.save(existingTweet);
    }

}