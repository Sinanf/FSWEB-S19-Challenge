package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Veritabanındaki "tweet" tablosunu temsil eder.
 * Uygulamanın en temel içerik birimidir.
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "tweet", schema = "public")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========================================================================
    // TEMEL ALANLAR (CONTENT & METADATA)
    // ========================================================================

    /**
     * Tweetin metin içeriği.
     * Twitter standardı gereği maksimum 280 karakter olabilir.
     */
    @Column(name = "content", length = 280, nullable = false)
    private String content;

    /**
     * Tweetin atıldığı tarih ve saat.
     * @CreationTimestamp sayesinde biz kodda elle set etmeyiz,
     * veritabanına kayıt anında sistem saatini otomatik alır.
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ========================================================================
    // RELATIONSHIPS (İLİŞKİLER)
    // ========================================================================

    /**
     * İlişki: Bir Kullanıcı -> Çok Tweet (ManyToOne)
     *
     * ÖNEMLİ: Cascade tipleri arasında "REMOVE" YOKTUR.
     * Çünkü bir tweet silinirse, o tweeti atan kullanıcı SİLİNMEMELİDİR.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    /**
     * RETWEET İLİŞKİSİ (Self-Referencing / Kendi Kendine Referans)
     *
     * Bir tweet, başka bir tweetin kopyası olabilir.
     * - Eğer bu alan null ise: Normal bir tweettir.
     * - Eğer bu alan dolu ise: Bu bir Retweettir ve `retweetOf` orijinal tweeti gösterir.
     */
    @ManyToOne
    @JoinColumn(name = "retweet_id") // Veritabanında "retweet_id" sütunu oluşur
    private Tweet retweetOf;

}