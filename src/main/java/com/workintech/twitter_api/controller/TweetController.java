package com.workintech.twitter_api.controller;

import com.workintech.twitter_api.dto.request.TweetRequest;
import com.workintech.twitter_api.dto.response.TweetResponse;
import com.workintech.twitter_api.entity.Tweet;
import com.workintech.twitter_api.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@CrossOrigin(origins = "*") // Frontend farklı portta olduğu için CORS izni veriyoruz
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    // ========================================================================
    // READ OPERATIONS (OKUMA İŞLEMLERİ)
    // ========================================================================

    /**
     * Tüm tweetleri listeler (Zenginleştirilmiş DTO formatında).
     * URL: GET http://localhost:8080/tweet
     */
    @GetMapping
    public List<TweetResponse> findAll() {
        return tweetService.findAllTweets();
    }


    @GetMapping("/findByUserId/{id}")
    public List<TweetResponse> findAllByUserId(@PathVariable long id) {
        // Yeni yazdığımız service metodunu çağırıyoruz
        return tweetService.findAllTweetsByUserId(id);
    }

    // ========================================================================
    // WRITE OPERATIONS (YAZMA İŞLEMLERİ)
    // ========================================================================

    /**
     * Yeni bir tweet atar.
     * URL: POST http://localhost:8080/tweet
     */
    @PostMapping
    public Tweet save(@Valid @RequestBody TweetRequest tweetRequest) {
        return tweetService.save(tweetRequest.getContent(), tweetRequest.getUserId());
    }

    /**
     * Var olan bir tweeti günceller.
     * URL: PUT http://localhost:8080/tweet/{id}
     */
    @PutMapping("/{id}")
    public Tweet update(@PathVariable Long id, @RequestBody TweetRequest tweetRequest) {
        // Request'ten sadece content'i alıp güncelliyoruz
        return tweetService.update(id, tweetRequest.getContent());
    }

    /**
     * Bir tweeti siler.
     * URL: DELETE http://localhost:8080/tweet/{id}
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        tweetService.delete(id);
        return "Tweet deleted successfully";
    }
}