package com.workintech.twitter_api.dto.response;

import java.time.LocalDateTime;

public record TweetResponse(
        Long id,
        String content,
        String userEmail,
        String userFirstName,
        int likeCount,
        int retweetCount,
        int commentCount, // YENİ: Yorum Sayısı
        boolean isLikedByMe,
        LocalDateTime createdAt
) {
}