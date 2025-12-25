package com.workintech.twitter_api.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String userFirstName,
        String userEmail,
        LocalDateTime createdAt
) {
}