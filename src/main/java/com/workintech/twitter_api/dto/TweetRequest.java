package com.workintech.twitter_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Frontend'den gelen "Tweet At" isteğini taşıyan ve doğrulayan veri objesi.
 * Tweet içeriğinin boş olup olmadığını ve karakter sınırını (280) burada kontrol ederiz.
 */
@Data
public class TweetRequest {

    // 1. KURAL: Tweet içeriği asla boş veya sadece boşluktan oluşamaz.
    @NotBlank(message = "Tweet içeriği boş olamaz!")
    // 2. KURAL: Twitter standardı gereği tweet en fazla 280 karakter olabilir.
    @Size(min = 1, max = 280, message = "Tweet 280 karakteri geçemez!")
    private String content;

    // Tweeti atan kullanıcının veritabanındaki ID'si
    private Long userId;

}