package com.workintech.twitter_api.service;

import com.workintech.twitter_api.entity.ApplicationUser;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.repository.ApplicationUserRepository;
import com.workintech.twitter_api.repository.CommentRepository;
import com.workintech.twitter_api.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TweetRepository tweetRepository, ApplicationUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public Comment save(String content, Long userId, Long tweetId) {
        // 1. Kullanıcıyı bul
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 2. Tweeti bul
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found with id: " + tweetId));

        // 3. Yorumu oluştur
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweet);

        // 4. Kaydet
        return commentRepository.save(comment);
    }
}