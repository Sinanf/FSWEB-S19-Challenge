package com.workintech.twitter_api.dto;

import lombok.Data;

@Data
public class TweetRequest {
    private String content;
    private Long userId;
}
