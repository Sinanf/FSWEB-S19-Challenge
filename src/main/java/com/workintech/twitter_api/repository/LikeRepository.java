package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Beğeni işlemleri için veritabanı kapısı.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Bir kullanıcı bu tweeti daha önce beğenmiş mi?
    // Varsa Like objesi döner, yoksa boş (Optional) döner.
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

    // Tweet bazlı toplam beğeni sayısını hesaplar.
    // SELECT COUNT(*) FROM likes WHERE tweet_id = ?
    int countByTweetId(Long tweetId);

}