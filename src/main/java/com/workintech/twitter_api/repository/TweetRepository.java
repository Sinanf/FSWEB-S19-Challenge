package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet,Long> {

    List<Tweet> findAllByUserId(Long userId);

    List<Tweet> findAllByOrderByCreatedAtDesc();

}
