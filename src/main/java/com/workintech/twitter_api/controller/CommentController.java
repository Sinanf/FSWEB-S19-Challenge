package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.request.CommentRequest;
import com.workintech.twitter_api.dto.response.CommentResponse;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*") // React uygulamasına erişim izni
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Yeni yorum ekleme (POST).
     * Frontend'den content, userId ve tweetId içeren bir JSON bekler.
     */
    @PostMapping
    public Comment save(@RequestBody CommentRequest req) {
        return commentService.save(req.getContent(), req.getUserId(), req.getTweetId());
    }

    /**
     * Bir tweete ait yorumları listeleme (GET).
     * Sonsuz döngüyü önlemek için Entity yerine CommentResponse (DTO) döner.
     */
    @GetMapping("/tweet/{tweetId}")
    public List<CommentResponse> getCommentsByTweet(@PathVariable Long tweetId) {
        return commentService.findCommentsByTweetId(tweetId);
    }

    /**
     * Yorum silme (DELETE).
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}