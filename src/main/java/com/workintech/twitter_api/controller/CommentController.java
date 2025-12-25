package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.response.CommentResponse;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*") // Frontend'den gelen isteklere izin ver
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Yorum Ekleme (POST)
    @PostMapping
    public Comment save(@RequestBody CommentRequest commentRequest) {
        // Not: CommentRequest diye bir DTO class'ın yoksa burayı map yapısıyla da alabilirsin
        // ama senin mevcut yapında muhtemelen Map veya özel bir sınıf kullanıyorsun.
        // Eğer hata alırsan aşağıda Map versiyonunu da paylaşıyorum.
        return commentService.save(commentRequest.content(), commentRequest.userId(), commentRequest.tweetId());
    }

    // --- İŞTE EKSİK OLAN KISIM BURASI ---
    // Frontend şu adrese istek atıyor: GET http://localhost:8080/comment/tweet/{id}
    @GetMapping("/tweet/{tweetId}")
    public List<CommentResponse> getCommentsByTweet(@PathVariable Long tweetId) {
        return commentService.findCommentsByTweetId(tweetId);
    }

    // Yorum Silme
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}

