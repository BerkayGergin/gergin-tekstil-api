package com.gergin.api.repository;

import com.gergin.api.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Ürünü kilitli (Pessimistic Lock) olarak getirir, başkası güncelleyemez
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(Long id);
}