package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity // Bu sınıfın Veritabanında bir tablo olduğunu belirtir (ORM).
@Table(name = "comment", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- İLİŞKİLER (RELATIONSHIPS) ---

    // İlişki: Bir yorumun TEK bir yazarı vardır. (Many Comments -> One User)
    // Önemli Detay: CascadeType.REMOVE kullanmadık.
    // Yani; "Yorum silinirse, yazarı SİLİNMEZ." (Veri güvenliği).
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // İlişki: Bir yorum TEK bir tweete aittir.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

    // --- OTOMATİK ZAMAN DAMGASI ---

    // Veritabanına INSERT atılmadan hemen önce bu metod çalışır.
    // Frontend'den tarih göndermeye gerek kalmaz, sunucu saatini basar.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}