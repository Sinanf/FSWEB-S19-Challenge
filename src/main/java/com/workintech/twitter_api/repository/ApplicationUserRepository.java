package com.workintech.twitter_api.repository;

import com.workintech.twitter_api.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Kullanıcı (ApplicationUser) tablosu ile veritabanı arasındaki köprüdür.
 * Spring Data JPA, buradaki metodlara göre otomatik SQL sorguları oluşturur.
 */
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Özel Sorgu Metodu:
     * Verilen Email adresine sahip kullanıcıyı veritabanında arar.
     * * @return Eğer kullanıcı varsa User nesnesi döner, yoksa boş (Empty) döner.
     */
    Optional<ApplicationUser> findByEmail(String email);

}