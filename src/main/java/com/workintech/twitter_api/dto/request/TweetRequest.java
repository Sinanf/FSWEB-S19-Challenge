package com.workintech.twitter_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // Lombok: Kod kalabalığını önler (Getter/Setter).
public class TweetRequest {

    // 1. Kural: Tweet boş olamaz, sadece boşluk karakteri (space) içeremez.
    @NotBlank(message = "Tweet içeriği boş olamaz!")

    // 2. Kural: Twitter standardı gereği 280 karakterden uzun olamaz.
    @Size(max = 280, message = "Tweet 280 karakteri geçemez!")
    private String content;

    // Tweeti atan kişinin ID'si
    private Long userId;
}