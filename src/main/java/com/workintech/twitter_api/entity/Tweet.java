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
    private Long id;

    @Column(name = "content")
    private String content;

    // --- SAYAÇLAR (Frontend verimliliği için) ---

    @Column(name = "like_count") // Veritabanındaki kolon adını açıkça belirttik
    private int likeCount = 0;

    @Column(name = "retweet_count")
    private int retweetCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- İLİŞKİLER ---

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "retweet_of_id")
    private Tweet retweetOf;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}