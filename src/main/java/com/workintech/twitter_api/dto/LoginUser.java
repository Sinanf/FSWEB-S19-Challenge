package com.workintech.twitter_api.dto;

import lombok.Data;

/**
 * Frontend'den gelen "Giriş Yap" (Login) isteğini karşılayan DTO.
 * Kullanıcı e-posta ve şifresini bu obje içinde gönderir.
 */
@Data
public class LoginUser {

    // Kullanıcının sisteme kayıtlı e-posta adresi
    private String email;

    // Kullanıcının girdiği şifre (Frontend'den ham gelir, Backend'de hash ile karşılaştırılır)
    private String password;

}