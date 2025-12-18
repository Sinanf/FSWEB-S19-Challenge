package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.TweetRequest;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    // Tweetleri listele
    @GetMapping
    public List<Tweet> findAll() {
        return tweetService.findAll();
    }

    // Bir kullanıcının tweetlerini getir: http://localhost:3000/tweet/findByUserId/1
    @GetMapping("/findByUserId/{id}")
    public List<Tweet> findAllByUserId(@PathVariable long id) {
        return tweetService.findAllByUserId(id);
    }

    // Tweet at: http://localhost:3000/tweet
    @PostMapping
    public Tweet save(@RequestBody TweetRequest tweetRequest) {
        return tweetService.save(tweetRequest.getContent(), tweetRequest.getUserId());
    }


    // Tweet Güncelleme: http://localhost:3000/tweet/1
    @PutMapping("/{id}")
    public Tweet update(@PathVariable Long id, @RequestBody TweetRequest tweetRequest) {
        // Request'ten sadece content'i alıyoruz
        return tweetService.update(id, tweetRequest.getContent());
    }

    // Tweet Silme: http://localhost:3000/tweet/1
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        tweetService.delete(id);
        return "Tweet deleted successfully";
    }
}