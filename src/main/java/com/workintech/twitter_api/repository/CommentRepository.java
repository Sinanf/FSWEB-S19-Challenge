package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Yorum (Comment) entity'si için veritabanı erişim katmanı.
 * JpaRepository sayesinde temel CRUD (Ekle, Sil, Güncelle, Listele) işlemleri otomatik olarak gelir.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByTweetId(Long tweetId);

    // Tweet ID'sine göre yorumları getirir
    List<Comment> findByTweetId(Long tweetId);

}