package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository: Veritabanı ile konuşan katman.
 * JpaRepository: Hazır "Save", "Delete", "FindById" metodlarını sağlar.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {


    // SELECT COUNT(*) FROM comment WHERE tweet_id = ?
    int countByTweetId(Long tweetId);

    // SELECT * FROM comment WHERE tweet_id = ?
    List<Comment> findByTweetId(Long tweetId);

}