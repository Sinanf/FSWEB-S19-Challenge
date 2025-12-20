package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}