package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.CommentRequest;
import com.workintech.twitter_api.entity.Comment;
import com.workintech.twitter_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST http://localhost:3000/comment
    @PostMapping
    public Comment save(@RequestBody CommentRequest commentRequest) {
        return commentService.save(
                commentRequest.getContent(),
                commentRequest.getUserId(),
                commentRequest.getTweetId()
        );
    }

    // PUT http://localhost:3000/comment/1
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public Comment update(@org.springframework.web.bind.annotation.PathVariable Long id,
                          @RequestBody CommentRequest commentRequest) {
        // Request'ten sadece content'i alıp güncelliyoruz
        return commentService.update(id, commentRequest.getContent());
    }

    // DELETE http://localhost:3000/comment/1
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public String delete(@org.springframework.web.bind.annotation.PathVariable Long id) {
        commentService.delete(id);
        return "Comment deleted successfully";
    }
}