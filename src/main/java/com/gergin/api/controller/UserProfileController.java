package com.gergin.api.controller;

import com.gergin.api.entity.User;
import com.gergin.api.entity.UserProfile;
import com.gergin.api.repository.UserProfileRepository;
import com.gergin.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;


    // Yardımcı Metod: Gelen değer ID mi yoksa Username mi çözer
    private User findUserByIdOrUsername(String identifier) {
        try {
            Long id = Long.parseLong(identifier);
            return userRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return userRepository.findByUsername(identifier).orElse(null);
        }
    }

    // 1. Profili Getir (ID veya Username ile çalışır)
    @GetMapping("/{identifier}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable String identifier) {
        User user = findUserByIdOrUsername(identifier);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
        if (profile == null) {
            UserProfile newProfile = new UserProfile();
            newProfile.setUser(user);
            profile = userProfileRepository.save(newProfile);
        }
        return ResponseEntity.ok(profile);
    }

    // 2. Profili Güncelle (ID veya Username ile çalışır)
    @PostMapping("/{identifier}")
    public ResponseEntity<?> updateProfile(@PathVariable String identifier, @RequestBody UserProfile updatedData) {
        try {
            User user = findUserByIdOrUsername(identifier);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(new UserProfile());
            if (profile.getUser() == null) {
                profile.setUser(user);
            }

            profile.setFullName(updatedData.getFullName());
            profile.setPhoneNumber(updatedData.getPhoneNumber());
            profile.setAddress(updatedData.getAddress());
            profile.setCity(updatedData.getCity());
            profile.setProfileImageUrl(updatedData.getProfileImageUrl());

            UserProfile savedProfile = userProfileRepository.save(profile);
            return ResponseEntity.ok(savedProfile);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Hata detayı: " + e.getMessage());
        }
    }
}