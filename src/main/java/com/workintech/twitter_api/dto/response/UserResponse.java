package com.workintech.twitter_api.dto.response;

// Pakete firstName, lastName ve avatar'ı da ekliyoruz
public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String avatar
) {}