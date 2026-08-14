package com.gergin.api.repository;

import com.gergin.api.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Kullanıcı ID'sine göre profil arama metodu
    Optional<UserProfile> findByUserId(Long userId);
}