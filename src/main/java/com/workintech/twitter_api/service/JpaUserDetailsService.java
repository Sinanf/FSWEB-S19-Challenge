package com.workintech.twitter_api.service;

import com.workintech.twitter_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security'nin varsayılan kullanıcı arama mekanizmasını
 * kendi veritabanımıza (PostgreSQL) bağladığımız sınıftır.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security, login işlemi sırasında bu metodu çağırır.
     * Veritabanına gidip kullanıcıyı email adresiyle bul.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // UserRepository üzerinden kullanıcıyı sorguluyoruz.
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı!"));
    }
}