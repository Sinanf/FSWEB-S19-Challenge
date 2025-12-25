package com.workintech.twitter_api.controller;

// Eğer CommentRequest sınıfın yoksa, dosyanın en altına şu record'u ekleyebilirsin:
record CommentRequest(String content, Long userId, Long tweetId) {
}
