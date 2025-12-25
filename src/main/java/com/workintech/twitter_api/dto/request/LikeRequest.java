package com.workintech.twitter_api.dto.request;

import lombok.Data;


@Data
public class LikeRequest {

    private Long userId;

    private Long tweetId;

}