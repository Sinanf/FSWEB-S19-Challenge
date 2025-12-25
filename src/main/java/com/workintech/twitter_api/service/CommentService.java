package com.workintech.twitter_api.service;

import com.workintech.twitter_api.dto.response.CommentResponse;
import com.workintech.twitter_api.entity.User;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.UserRepository;
import com.workintech.twitter_api.repository.CommentRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          TweetRepository tweetRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // --- YORUM KAYDETME ---
    public Comment save(String content, Long userId, Long tweetId) {
        // İlgili kullanıcıyı ve tweeti bul, yoksa hata fırlat.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı: " + tweetId));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);   // Yorum ile Kullanıcıyı bağlıyoruz.
        comment.setTweet(tweet); // Yorum ile Tweeti bağlıyoruz.

        return commentRepository.save(comment);
    }

    // --- YORUM SİLME (GÜVENLİ) ---
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı!"));

        // 1. Giriş yapmış olan kullanıcının emailini SecurityContext'ten alıyoruz.
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. YETKİ KONTROLÜ: Yorumun sahibi ile giriş yapan kişi aynı mı?
        if (!comment.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Bu yorumu silmeye yetkiniz yok!");
        }
        commentRepository.delete(comment);
    }

    // --- TWEETE ÖZEL YORUMLARI LİSTELEME ---
    public List<CommentResponse> findCommentsByTweetId(Long tweetId) {
        List<Comment> comments = commentRepository.findByTweetId(tweetId);

        // Entity listesini, frontend'in beklediği Response DTO listesine çeviriyoruz.
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getUser().getEmail(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    public Comment update(Long id, String newContent) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        existingComment.setContent(newContent);
        return commentRepository.save(existingComment);
    }
}