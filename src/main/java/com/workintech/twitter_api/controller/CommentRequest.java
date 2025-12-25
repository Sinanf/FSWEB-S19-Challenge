package com.workintech.twitter_api.controller;


public record CommentRequest(String content, Long userId, Long tweetId) {

}
