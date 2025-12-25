package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tweet", schema = "public")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    // --- SAYAÇLAR (Frontend'de göstermek için) ---
    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "retweet_count")
    private int retweetCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    // --- TARİH AYARI (Sorunu çözecek kısım) ---
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- İLİŞKİLER ---

    // DİKKAT: Artık ApplicationUser değil, User sınıfına bağlıyoruz.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bir tweetin altındaki yorumlar (Silinirse yorumlar da gider)
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // Bir tweet başka bir tweetin retweet'i olabilir
    @ManyToOne
    @JoinColumn(name = "retweet_of_id")
    private Tweet retweetOf;

    // --- LIKES İLİŞKİSİ (Hangi kullanıcılar beğendi) ---
    // Eğer Like tablosunu ayrı tutuyorsan burası değişebilir ama
    // genelde ManyToMany veya ayrı bir entity ile yapılır.
    // Şimdilik basitlik adına likeCount üzerinden gidiyoruz.
    // Eğer "Like" entity'si kullanıyorsan onu buraya List<Like> olarak eklemelisin.

    // --- OTOMATİK TARİH ATAMA ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // Null check yapalım ki sayaçlar null gelmesin
        if (this.likeCount < 0) this.likeCount = 0;
    }
}