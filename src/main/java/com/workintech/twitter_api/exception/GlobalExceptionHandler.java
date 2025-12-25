package com.workintech.twitter_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Tüm projede fırlatılan hataları tek merkezden yakalar.
public class GlobalExceptionHandler {

    // Frontend'e gidecek standart hata formatı.
    public record ErrorResponse(int status, String message, long timestamp) {}

    // --- ÖZEL HATALARI YAKALA (400 BAD REQUEST) ---
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exc) {
        // Bizim Service katmanında throw ettiğimiz mesajları (örn: "Tweet bulunamadı") yakalar.
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // --- BEKLENMEDİK SİSTEM HATALARI (500 INTERNAL SERVER ERROR) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exc) {
        // Kodda öngöremediğimiz (NullPointer vb.) ciddi sistem hatalarını yakalar.
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Beklenmedik bir hata oluştu: " + exc.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}