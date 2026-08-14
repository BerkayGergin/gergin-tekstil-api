package com.gergin.api.controller;

import com.gergin.api.entity.User;
import com.gergin.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Yönetici hesabı kontrolü ve BCrypt şifreleme onarımı
    @PostConstruct
    public void initSuperAdmin() {
        User admin = userRepository.findByUsername("superadmin").orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setUsername("superadmin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        } else if (!admin.getPassword().startsWith("$2a$")) {
            // Şifre önceki testlerden dolayı düz metin kalmışsa BCrypt formatına dönüştürür
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }

    // Giriş Yapma İşlemi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        // Şifre kontrolünü BCrypt makinesi üzerinden yapıyoruz
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Kullanıcı adı veya şifre hatalı.");
    }

    // Kayıt Olma İşlemi
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten alınmış.");
        }

        // Yeni kullanıcının şifresini veritabanına kaydetmeden önce BCrypt ile şifreliyoruz
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole("USER");
        User savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }
}