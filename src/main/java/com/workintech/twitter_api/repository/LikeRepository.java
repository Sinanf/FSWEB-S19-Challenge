package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // "Bana şu userId ve şu tweetId'ye sahip like'ı bul" (Varsa döner)
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
}