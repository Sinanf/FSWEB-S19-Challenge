package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // PROFESYONEL ARAMA SORGUSU:
    // Ad, Soyad, Username veya Email içinde arama yapar.
    @Query("SELECT u FROM User u WHERE " +
            "lower(u.firstName) LIKE lower(concat('%', :query, '%')) OR " +
            "lower(u.lastName) LIKE lower(concat('%', :query, '%')) OR " +
            "lower(u.username) LIKE lower(concat('%', :query, '%')) OR " +
            "lower(u.email) LIKE lower(concat('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);
}