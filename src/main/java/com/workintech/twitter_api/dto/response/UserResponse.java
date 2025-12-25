package com.workintech.twitter_api.dto.response;

/**
 * Kullanıcı Yanıtı (DTO).
 * Arama sonuçlarında veya profil sayfasında Frontend'e gönderdiğimiz güvenli kullanıcı objesi.
 * DİKKAT: İçinde 'password' alanı YOKTUR. Güvenlik için bilerek çıkarılmıştır.
 */
public record UserResponse(
        Long id,
        String username, // Kullanıcı adı (@deneme)
        String email,
        String firstName,
        String lastName,
        String avatar
) {}