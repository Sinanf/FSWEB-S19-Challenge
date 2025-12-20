package com.workintech.twitter_api.dto;

import java.time.LocalDateTime;

/**
 * Frontend'e gönderilecek olan "Zenginleştirilmiş" Tweet verisi.
 * Java "Record" özelliği sayesinde getter/setter yazmaya gerek kalmaz, veriler sabittir (Immutable).
 */
public record TweetResponse(

        // Tweetin benzersiz kimlik numarası
        Long id,

        // Tweetin metin içeriği
        String content,

        // Tweeti atan kullanıcının ekranda görünecek bilgileri
        String userEmail,
        String userFirstName,

        // Tweetin etkileşim sayıları (Hesaplanarak buraya konur)
        int likeCount,
        int retweetCount,

        // Frontend'de kalp ikonunun kırmızı mı boş mu olacağını belirleyen bilgi.
        // True gelirse kullanıcı daha önce beğenmiştir.
        boolean isLikedByMe,

        // Tweetin atıldığı tarih ve saat (Sıralama için kullanılır)
        LocalDateTime createdAt

) {
}