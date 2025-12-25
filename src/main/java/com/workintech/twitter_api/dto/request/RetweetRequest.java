package com.workintech.twitter_api.dto.request;

import lombok.Data;

@Data
public class RetweetRequest {

    // Retweet yapan kullanıcının ID'si
    private Long userId;

    // Retweetlenen (paylaşılan) orijinal tweetin ID'si
    private Long tweetId;

}