package com.gergin.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String category;
    private Integer stock;
    private Integer stockQuantity;
    private String imageUrl;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String sizeStock;

    @Version
    private Long version;

    public Product() {
    }

    public Product(String name, Double price, String category, Integer stock, Integer stockQuantity, String imageUrl, String description, String sizeStock) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.description = description;
        this.sizeStock = sizeStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Birbirlerini ezmemeleri için bağımsızlaştırıldı
    public Integer getStock() {
        return stock != null ? stock : (stockQuantity != null ? stockQuantity : 0);
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockQuantity() {
        return stockQuantity != null ? stockQuantity : (stock != null ? stock : 0);
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSizeStock() {
        return sizeStock;
    }

    public void setSizeStock(String sizeStock) {
        this.sizeStock = sizeStock;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}