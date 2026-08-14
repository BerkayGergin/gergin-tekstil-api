package com.gergin.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcı adı benzersiz (unique) olmalı ve boş bırakılamaz
    @Column(unique = true, nullable = false)
    private String username;

    // Şifreler ileride kriptolanarak (hash) saklanacak
    @Column(nullable = false)
    private String password;

    // Kullanıcının yetkisi: "ADMIN" veya "USER" olacak
    @Column(nullable = false)
    private String role;
}