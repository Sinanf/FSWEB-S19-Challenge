package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.LikeRequest;
import com.workintech.twitter_api.entity.Like;
import com.workintech.twitter_api.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // POST http://localhost:3000/like
    @PostMapping("/like")
    public Like likeTweet(@RequestBody LikeRequest likeRequest) {
        return likeService.addLike(likeRequest);
    }

    // POST http://localhost:3000/dislike
    @PostMapping("/dislike")
    public String dislikeTweet(@RequestBody LikeRequest likeRequest) {
        likeService.removeLike(likeRequest);
        return "Like removed successfully";
    }
}