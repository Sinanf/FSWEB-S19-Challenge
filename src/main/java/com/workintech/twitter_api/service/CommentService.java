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

    public Comment save(String content, Long userId, Long tweetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found with id: " + tweetId));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweet);

        // createdAt @PrePersist ile otomatik atanacak.
        return commentRepository.save(comment);
    }

    // --- GÜNCELLEME ve SİLME metodların aynen kalabilir ---
    public Comment update(Long id, String newContent) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        existingComment.setContent(newContent);
        return commentRepository.save(existingComment);
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!comment.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this comment!");
        }
        commentRepository.delete(comment);
    }


    public List<CommentResponse> findCommentsByTweetId(Long tweetId) {
        List<Comment> comments = commentRepository.findByTweetId(tweetId);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        // DÜZELTME 1: User entity'sinde firstName yok, o yüzden 'username'i gönderiyoruz.
                        // Frontend'de "userFirstName" olarak karşılanacak.
                        comment.getUser().getUsername(),
                        comment.getUser().getEmail(),
                        // DÜZELTME 2: Artık yorumun kendi tarihini gönderiyoruz.
                        comment.getCreatedAt()
                ))
                .toList();
    }
}