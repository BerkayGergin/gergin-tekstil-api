package com.gergin.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data // Lombok kütüphanesi sayesinde Getter/Setter yazma hamallığından kurtuluyoruz
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    /// Ürünün Stok Miktarı (int yerine Integer kullanıyoruz ki eski boş verilerde hata vermesin)
    private Integer stock;

    // Getter ve Setter Metodları
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    private Integer stockQuantity;

    private String size;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}