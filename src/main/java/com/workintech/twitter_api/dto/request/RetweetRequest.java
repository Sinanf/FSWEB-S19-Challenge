package com.workintech.twitter_api.dto.request;

import lombok.Data;

@Data
public class RetweetRequest {

    private Long userId;

    private Long tweetId;

}