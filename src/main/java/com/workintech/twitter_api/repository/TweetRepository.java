package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Tweet entity'si için veritabanı erişim katmanı.
 * Tweetlerin listelenmesi, filtrelenmesi ve sıralanması işlemlerini yönetir.
 */
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    /**
     * Kullanım: Profil sayfasında sadece o kişinin tweetlerini listelemek için.
     */
    List<Tweet> findAllByUserId(Long userId);

    /**
     * Kullanım: Ana sayfa akışında (Feed) en güncel tweetin en üstte görünmesi için.
     */
    List<Tweet> findAllByOrderByCreatedAtDesc();

    List<Tweet> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    int countByRetweetOfId(Long tweetId);


}