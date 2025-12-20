package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.CommentRequest;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ========================================================================
    // CRUD OPERATIONS (EKLEME, GÜNCELLEME, SİLME)
    // ========================================================================

    /**
     * Bir tweete yeni yorum ekler.
     * URL: POST http://localhost:8080/comment
     */
    @PostMapping
    public Comment save(@RequestBody CommentRequest commentRequest) {
        return commentService.save(
                commentRequest.getContent(),
                commentRequest.getUserId(),
                commentRequest.getTweetId()
        );
    }

    /**
     * Var olan bir yorumu günceller.
     * URL: PUT http://localhost:8080/comment/{id}
     */
    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        // Request'ten sadece content'i alıp güncelliyoruz
        return commentService.update(id, commentRequest.getContent());
    }

    /**
     * Bir yorumu siler.
     * URL: DELETE http://localhost:8080/comment/{id}
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        commentService.delete(id);
        return "Comment deleted successfully";
    }
}