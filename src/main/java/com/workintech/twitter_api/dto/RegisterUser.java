package com.workintech.twitter_api.dto;

import lombok.Data;

@Data
public class RegisterUser {
    private String username;
    private String email;
    private String password;
}
