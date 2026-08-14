package com.gergin.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API (Next.js ile haberleşme) yaptığımız için CSRF korumasını kapatıyoruz
                .csrf(csrf -> csrf.disable())

                // @CrossOrigin notasyonlarının çalışabilmesi için CORS'u varsayılan ayarlarla açıyoruz
                .cors(Customizer.withDefaults())

                // Gelen isteklerin (URL'lerin) izinlerini ayarlıyoruz
                .authorizeHttpRequests(auth -> auth
                        // Vitrindeki ürünleri herkes görebilir (Sadece GET istekleri serbest)
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // ŞİMDİLİK sistem kilitlenmesin diye diğer tüm isteklere de izin veriyoruz.
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    // Şifreleri kriptolamak için kullanacağımız makine (BCrypt)
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    // Next.js (localhost:3000) bağlantısına izin veren CORS yapılandırması
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}