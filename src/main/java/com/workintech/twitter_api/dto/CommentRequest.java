package com.workintech.twitter_api.dto;

import lombok.Data;

/**
 * Frontend'den gelen "Yorum Yap" isteğini karşılayan veri transfer objesi.
 * Kullanıcı bir tweete yorum yazdığında bu veriler Backend'e ulaşır.
 */
@Data
public class CommentRequest {

    // Yorumun metin içeriği (Örn: "Çok güzel paylaşım!")
    private String content;

    // Yorumu yazan kullanıcının veritabanındaki ID'si
    private Long userId;

    // Hangi tweete yorum yapıldığı (Tweet ID)
    private Long tweetId;

}