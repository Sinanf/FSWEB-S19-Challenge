package com.workintech.twitter_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Hata Yönetimi Sınıfı.
 * Tüm Controller'lardan fırlatılan istisnaları (Exception) burada yakalar
 * ve kullanıcıya anlaşılır bir JSON cevabı döneriz.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // ========================================================================
    // ERROR RESPONSE MODEL (HATA CEVAP İSKELETİ)
    // ========================================================================

    /**
     * Hata oluştuğunda Frontend'e dönecek olan veri formatı.
     * Örn: { "status": 400, "message": "User not found", "timestamp": 16789... }
     */
    public record ErrorResponse(int status, String message, long timestamp) {}

    // ========================================================================
    // EXCEPTION HANDLERS (HATA YAKALAYICILAR)
    // ========================================================================

    /**
     * RuntimeException Yakalayıcı.
     * Bizim kodumuzda bilerek fırlattığımız iş mantığı hatalarını yakalar.
     * Örn: "User not found", "Tweet 280 karakterden uzun olamaz", "Yetkiniz yok" vb.
     * Durum Kodu: 400 BAD REQUEST
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(), // Fırlattığımız hatadaki mesajı (örn: "Kullanıcı bulunamadı") buraya koyar
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Genel Exception Yakalayıcı.
     * Öngöremediğimiz, sistemden kaynaklı diğer tüm hataları yakalar.
     * Örn: NullPointerException, Veritabanı bağlantı hatası vb.
     * Durum Kodu: 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exc) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exc.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}