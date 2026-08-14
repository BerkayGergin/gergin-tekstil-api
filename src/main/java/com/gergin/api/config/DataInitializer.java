package com.gergin.api; // veya com.gergin.api.config, nereye açtıysan

import com.gergin.api.entity.User;
import com.gergin.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // "superadmin" adında bir kullanıcı var mı diye kontrol et
            if (userRepository.findByUsername("superadmin").isEmpty()) {

                User admin = new User();
                admin.setUsername("superadmin");
                // Şifresini 123456 yapıyoruz (Sen istersen değiştirebilirsin)
                admin.setPassword(passwordEncoder.encode("123456"));
                // Ve işte o sihirli kelime: ADMIN
                admin.setRole("ADMIN");

                userRepository.save(admin);
                System.out.println("Süper Admin hesabı başarıyla oluşturuldu! (Kullanıcı: superadmin)");
            }
        };
    }
}