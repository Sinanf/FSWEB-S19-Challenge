package com.workintech.twitter_api.dto.request;

import lombok.Data;

@Data
public class CommentRequest {


    private String content;

    private Long userId;

    private Long tweetId;

}