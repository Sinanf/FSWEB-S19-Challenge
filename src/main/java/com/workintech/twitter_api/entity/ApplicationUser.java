package com.workintech.twitter_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Bu sınıf hem veritabanındaki "app_user" tablosunu oluşturur,
 * hem de Spring Security'nin kullanıcı doğrulama (Auth) işlemleri için
 * ihtiyaç duyduğu `UserDetails` kurallarını uygular.
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "app_user", schema = "public") // PostgreSQL'de "user" kelimesi yasaklı olduğu için tablo adı "app_user" yapıldı.
public class ApplicationUser implements UserDetails {

    // ========================================================================
    // DATABASE FIELDS (VERİTABANI SÜTUNLARI)
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Kullanıcının ekranda görünen adı (Örn: Ali Veli)
    @Column(name = "username", nullable = false)
    private String username;

    // Kullanıcının giriş yaparken kullandığı benzersiz adresi
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Şifrelenmiş (Hashed) parola saklanır
    @Column(name = "password", nullable = false)
    private String password;

    // ========================================================================
    // RELATIONSHIPS (TABLO İLİŞKİLERİ)
    // ========================================================================

    /**
     * Bir kullanıcının birden çok tweeti olabilir (One-To-Many).
     * @JsonIgnore: Kullanıcıyı çektiğimizde tweetleri, tweetleri çektiğimizde kullanıcıyı getirip
     * sonsuz döngüye (Infinite Recursion) girmemesi için bu listeyi JSON çıktısında gizliyoruz.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tweet> tweets;

    // ========================================================================
    // SPRING SECURITY IMPLEMENTATION (UserDetails AYARLARI)
    // ========================================================================

    /**
     * Kullanıcının yetkilerini (Rollerini) döndürür.
     * Şimdilik Admin/User gibi rol ayrımı yapmadığımız için boş liste dönüyoruz.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * ÖNEMLİ: Spring Security varsayılan olarak "username" alanını giriş için kullanır.
     * Ancak biz giriş yaparken E-Posta kullanmak istiyoruz.
     * Bu yüzden getUsername() metodunun E-Mail döndürmesini sağlıyoruz.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    // Aşağıdaki metodlar hesabın durumuyla ilgilidir.
    // Şimdilik hepsini "true" yaparak tüm hesapları aktif ve sorunsuz kabul ediyoruz.

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hesap süresi dolmadı
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Hesap kilitli değil
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Şifre süresi dolmadı
    }

    @Override
    public boolean isEnabled() {
        return true; // Hesap aktif
    }
}