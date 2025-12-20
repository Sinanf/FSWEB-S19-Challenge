package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Veritabanındaki "comment" tablosunu temsil eder.
 * Kullanıcıların tweetlere yazdığı yanıtlar burada tutulur.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "comment", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Yorumun metin içeriği
    @Column(name = "content")
    private String content;

    // ========================================================================
    // RELATIONSHIPS (İLİŞKİLER)
    // ========================================================================

    /**
     * İlişki: Bir Kullanıcı -> Çok Yorum (ManyToOne)
     *
     * ÖNEMLİ NOT: Cascade tipleri arasında "REMOVE" bilerek eklenmemiştir.
     * Çünkü bir yorum silindiğinde, o yorumu yazan KULLANICI silinmemelidir.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    /**
     * İlişki: Bir Tweet -> Çok Yorum (ManyToOne)
     *
     * Aynı şekilde, yorum silindiğinde ana TWEET silinmemelidir.
     * Sadece yorumun kendisi yok olmalıdır.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;
}