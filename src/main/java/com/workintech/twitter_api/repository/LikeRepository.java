package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Beğeni (Like) entity'si için veritabanı erişim katmanı.
 * Kullanıcıların beğenilerini sorgulamak ve saymak için kullanılır.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {


    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

    /**
     * Tweet listelenirken "Like Count" bilgisini hesaplamak için kullanılır.
     */
    int countByTweetId(Long tweetId);

}