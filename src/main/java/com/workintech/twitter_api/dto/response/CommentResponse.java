package com.workintech.twitter_api.dto.response;

import java.time.LocalDateTime;

/**
 * Yorum Yanıtı (Response DTO).
 * Backend'den Frontend'e yorum listesi gönderilirken kullanılır.
 * * Amaç: Comment Entity'sini doğrudan dönersek, içindeki User objesi yüzünden
 * sonsuz döngü (User -> Comment -> User...) oluşur.
 * Bu DTO ile sadece gerekli verileri seçip gönderiyoruz.
 */
public record CommentResponse(
        Long id,
        String content,
        String userFirstName,
        String userEmail,
        LocalDateTime createdAt
) {
}