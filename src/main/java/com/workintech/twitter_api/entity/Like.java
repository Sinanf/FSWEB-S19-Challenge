package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Veritabanındaki "likes" tablosunu temsil eder.
 * Bu tablo, Kullanıcılar ve Tweetler arasındaki "Beğeni" ilişkisini tutar.
 * Aslında bir "Çoktan Çoğa" (Many-to-Many) ilişkinin ara tablosudur.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "likes", schema = "public") // "LIKE" SQL'de rezerve kelime olduğu için tablo adı "likes" olmalı
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========================================================================
    // RELATIONSHIPS (İLİŞKİLER)
    // ========================================================================

    /**
     * Beğenme işlemini yapan kullanıcı.
     * Bir kullanıcı birden çok like atabilir.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    /**
     * Beğenilen tweet.
     * Bir tweet birden çok kez beğenilebilir.
     */
    @ManyToOne
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

}