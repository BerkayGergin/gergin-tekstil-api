package com.gergin.api.controller;

import com.gergin.api.entity.User;
import com.gergin.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Next.js'e izin veriyoruz
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Az önce config'de ürettiğimiz kriptolama makinesi

    // Yeni kullanıcı kayıt etme (Register)
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // 1. Önce kullanıcının girdiği şifreyi al ve kriptola
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 2. Eğer rol belirtilmemişse varsayılan olarak "USER" (Müşteri) yap
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");

        }

        // 3. Veritabanına kaydet
        userRepository.save(user);

        return "Kayıt başarılı! Hoş geldin, " + user.getUsername();
    }
    // Kullanıcı Girişi (Login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        // 1. Veritabanından kullanıcıyı adına göre bul
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        // 2. Kullanıcı yoksa VEYA şifre eşleşmiyorsa hata fırlat
        // passwordEncoder.matches() -> Giren düz şifre ile veritabanındaki şifreli metni kıyaslar
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Kullanıcı adı veya şifre yanlış!");
        }

        // 3. Giriş başarılıysa, Frontend tarafına bir "Kimlik Kartı" gönder (Adı ve Yetkisi)
        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("message", "Giriş başarılı!");

        return ResponseEntity.ok(response);
    }
}