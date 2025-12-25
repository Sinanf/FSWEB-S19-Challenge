package com.workintech.twitter_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TweetRequest {
    @NotBlank(message = "Tweet içeriği boş olamaz!")
    @Size(max = 280, message = "Tweet 280 karakteri geçemez!")
    private String content;
    private Long userId;
}