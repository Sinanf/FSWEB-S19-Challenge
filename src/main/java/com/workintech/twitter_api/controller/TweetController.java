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
@CrossOrigin(origins = "*") // React (farklı port) erişimi için şart.
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    // --- OKUMA İŞLEMLERİ (READ) ---

    @GetMapping
    public List<TweetResponse> findAll() {
        // Anasayfa akışı. Entity değil DTO dönüyoruz (Şifreler gizlensin diye).
        return tweetService.findAllTweets();
    }

    @GetMapping("/findByUserId/{id}")
    public List<TweetResponse> findAllByUserId(@PathVariable long id) {
        // Profil sayfası: Sadece o kullanıcının tweetlerini getir.
        return tweetService.findAllTweetsByUserId(id);
    }

    // --- YAZMA İŞLEMLERİ (WRITE) ---

    @PostMapping
    public Tweet save(@Valid @RequestBody TweetRequest tweetRequest) {
        // Tweet atma. @Valid: İçerik boş mu diye kontrol eder.
        return tweetService.save(tweetRequest.getContent(), tweetRequest.getUserId());
    }

    @PutMapping("/{id}")
    public Tweet update(@PathVariable Long id, @RequestBody TweetRequest tweetRequest) {
        // Güncelleme: Sadece metni (content) değiştiriyoruz.
        return tweetService.update(id, tweetRequest.getContent());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        // Silme işlemi.
        tweetService.delete(id);
        return "Tweet silindi.";
    }
}