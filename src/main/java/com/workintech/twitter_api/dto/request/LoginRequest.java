package com.workintech.twitter_api.dto.request;

/**
 * Giriş İsteği DTO'su.
 * Java "Record" yapısı kullanılarak tanımlanmıştır.
 * Bu sayede Getter, Setter, Constructor, toString gibi metodları yazmaya gerek kalmaz.
 * Frontend'den gelen { "email": "...", "password": "..." } JSON verisini karşılar.
 */
public record LoginRequest(String email, String password) {
}