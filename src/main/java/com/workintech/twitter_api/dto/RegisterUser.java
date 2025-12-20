package com.workintech.twitter_api.dto;

import lombok.Data;

/**
 * Frontend'den gelen "Kayıt Ol" (Register) isteğini karşılayan DTO.
 * Yeni kullanıcı oluşturulurken gerekli temel bilgileri taşır.
 */
@Data
public class RegisterUser {

    // Kullanıcının sistemde görünecek adı (Örn: "Ali Veli")
    private String username;

    // Giriş yaparken kullanılacak benzersiz e-posta adresi
    private String email;

    // Kullanıcının belirlediği ham şifre (Veritabanına kaydedilmeden önce şifrelenecek)
    private String password;

}