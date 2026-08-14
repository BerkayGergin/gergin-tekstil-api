package com.gergin.api.repository;

import com.gergin.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // JpaRepository bizim için save(), findAll(), findById(), deleteById()
    // gibi tüm temel veritabanı işlemlerini arka planda otomatik olarak yazar.
    // İleride buraya "kategoriye göre ürünleri getir" gibi özel metodlar da ekleyeceğiz.

}