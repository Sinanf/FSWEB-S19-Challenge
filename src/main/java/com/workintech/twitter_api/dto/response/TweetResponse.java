package com.workintech.twitter_api.dto.response;

import java.time.LocalDateTime;

/**
 * Tweet Yanıtı (Response DTO).
 * Frontend'in bir tweeti ekrana basmak için ihtiyaç duyduğu TÜM veriyi tek pakette yollar.
 * İlişkisel veriler (User, Like, Comment) burada düzleştirilir (Flattening).
 */
public record TweetResponse(
        Long id,
        String content,

        // Tweeti atanın bilgileri (Entity yerine sadece lazım olanları aldık)
        String userEmail,
        String userFirstName,
        String userLastName,
        String userAvatar,

        // İstatistikler (Frontend'de sayıları göstermek için)
        int likeCount,
        int retweetCount,
        int commentCount,


        // Frontend'deki kalp ikonunun kırmızı mı yoksa boş mu olacağını bu belirler.
        boolean isLikedByMe,

        LocalDateTime createdAt
) {
}