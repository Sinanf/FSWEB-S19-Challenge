package com.workintech.twitter_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "likes", schema = "public") // 'Like' SQL'de rezerve bir kelime olduğu için tablo adını 'likes' yaptık.
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // --- İLİŞKİLER (RELATIONSHIPS) ---

    // Beğeniyi yapan kullanıcı. (Bir kullanıcının çok sayıda beğenisi olabilir)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Beğenilen tweet. (Bir tweetin çok sayıda beğenisi olabilir)
    @ManyToOne
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

}