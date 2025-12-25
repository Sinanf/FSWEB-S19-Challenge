package com.workintech.twitter_api.dto.request;

import lombok.Data;

@Data
public class CommentRequest {

    // Frontend'den gelen yorum metni
    private String content;

    // Yorumu yapan kişinin ID'si
    private Long userId;

    // Yorumun yapıldığı tweet'in ID'si
    private Long tweetId;

}