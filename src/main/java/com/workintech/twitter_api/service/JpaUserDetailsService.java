package com.workintech.twitter_api.service;

import com.workintech.twitter_api.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security'nin kullanıcı doğrulama (Authentication) sürecinde
 * kullanıcı verilerine erişmek için kullandığı servis sınıfıdır.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(ApplicationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security, giriş yapılmaya çalışıldığında bu metodu çağırır.
     * Biz sistemde Username yerine Email kullandığımız için aramayı email ile yapıyoruz.
     * * @param email Giriş formundan gelen e-posta adresi
     * @return Spring Security'nin anlayabileceği UserDetails nesnesi (ApplicationUser)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Veritabanında bu email ile kayıtlı kullanıcı var mı bak
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }
}