package com.workintech.twitter_api.dto;

import lombok.Data;

/**
 * Frontend'den gelen "Retweet" isteğini taşıyan veri objesi.
 * Hangi kullanıcının hangi tweeti paylaşmak istediği bilgisini içerir.
 */
@Data
public class RetweetRequest {

    // Retweet işlemini yapan kullanıcının ID'si
    private Long userId;

    // Retweetlenen (paylaşılan) orijinal tweetin ID'si
    private Long tweetId;

}