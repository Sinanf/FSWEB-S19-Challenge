package com.workintech.twitter_api.dto.request;

/**
 * Kayıt Olma İsteği (DTO).
 * Frontend'deki Kayıt Formu'ndan gelen verileri karşılar.
 * Record yapısı olduğu için veriler değiştirilemez (Immutable) ve güvenlidir.
 */
public record RegisterRequest(String firstName,
                              String lastName,
                              String email,
                              String password) {
}