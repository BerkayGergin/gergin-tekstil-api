package com.gergin.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productName;
    private String userName;
    private int rating;

    @Column(length = 1000)
    private String comment;

    private String date;
    private String status; // "ONAYLANDI", "BEKLEMEDE", "GİZLENDİ"

    @Column(length = 1000)
    private String adminReply;

    public Review() {
    }

    public Review(Long productId, String productName, String userName, int rating, String comment, String date, String status, String adminReply) {
        this.productId = productId;
        this.productName = productName;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
        this.adminReply = adminReply;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }
}