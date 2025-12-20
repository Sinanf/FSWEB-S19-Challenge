package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.repository.CommentRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Yorum işlemlerinin (Ekleme, Silme, Güncelleme) iş mantığını yöneten servis.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          TweetRepository tweetRepository,
                          ApplicationUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // ========================================================================
    // CREATE OPERATION (YORUM EKLEME)
    // ========================================================================

    /**
     * Bir tweete yeni bir yorum kaydeder.
     */
    public Comment save(String content, Long userId, Long tweetId) {
        // 1. Yazarı ve ilgili tweeti doğrula
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found with id: " + tweetId));

        // 2. Yorum nesnesini oluştur ve ilişkileri kur
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweet);

        // 3. Veritabanına kaydet
        return commentRepository.save(comment);
    }

    // ========================================================================
    // UPDATE OPERATION (GÜNCELLEME)
    // ========================================================================

    /**
     * Var olan bir yorumun içeriğini değiştirir.
     */
    public Comment update(Long id, String newContent) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        existingComment.setContent(newContent);
        return commentRepository.save(existingComment);
    }

    // ========================================================================
    // DELETE OPERATION (SİLME & YETKİ KONTROLÜ)
    // ========================================================================

    /**
     * Bir yorumu siler.
     * ÖNEMLİ: Yorumu sadece yazan kişi silebilir.
     */
    public void delete(Long id) {
        // 1. Silinecek yorumu bul
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        // 2. Güvenlik: Giriş yapan kullanıcıyı SecurityContext üzerinden al
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ApplicationUser loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 3. Yetki Kontrolü: Yorum sahibi ile giriş yapan kişi aynı mı?
        if (!comment.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this comment!");
        }

        // 4. Onaylandıysa sil
        commentRepository.delete(comment);
    }
}