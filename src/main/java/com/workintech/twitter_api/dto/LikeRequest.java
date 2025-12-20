package com.workintech.twitter_api.dto;

import lombok.Data;

/**
 * Frontend'den gelen "Beğeni (Like)" isteğini taşıyan veri objesi.
 */
@Data
public class LikeRequest {

    // Beğenme işlemini yapan kullanıcının ID'si
    private Long userId;

    // Beğenilen tweetin ID'si
    private Long tweetId;

}