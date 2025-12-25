package com.workintech.twitter_api.dto.request;

import lombok.Data;

@Data
public class LikeRequest {

    // Beğenen kişinin ID'si
    private Long userId;

    // Beğenilen tweet'in ID'si
    private Long tweetId;

}