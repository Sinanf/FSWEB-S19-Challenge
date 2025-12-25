package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Tweetler için veritabanı yönetim merkezi.
 * Metod isimleri üzerinden otomatik SQL sorguları üretir.
 */
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Profil Sayfası: Belirli bir kullanıcının tweetlerini getirir.
    List<Tweet> findAllByUserId(Long userId);

    // Anasayfa Akışı: Tüm tweetleri YENİDEN ESKİYE doğru sıralar.
    // SELECT * FROM tweet ORDER BY created_at DESC
    List<Tweet> findAllByOrderByCreatedAtDesc();

    // Kullanıcı Akışı: Sadece o kişinin tweetlerini tarihe göre sıralı getirir.
    List<Tweet> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    // Retweet Sayacı: Bu tweetin kaç kez retweetlendiğini hesaplar.
    int countByRetweetOfId(Long tweetId);
}