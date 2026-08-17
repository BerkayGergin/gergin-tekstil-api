package com.gergin.api.controller;

import com.gergin.api.entity.Review;
import com.gergin.api.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Tüm Yorumları Getir (Admin Paneli)
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Ürüne Göre Onaylanmış Yorumları Getir (Vitrin & Detay Sayfası)
    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProduct(@PathVariable Long productId) {
        return reviewRepository.findByProductIdAndStatus(productId, "ONAYLANDI");
    }

    // Yeni Yorum Ekle
    @PostMapping
    public Review createReview(@RequestBody Review review) {
        if (review.getStatus() == null || review.getStatus().isEmpty()) {
            review.setStatus("ONAYLANDI");
        }
        return reviewRepository.save(review);
    }

    // Yorum Düzenle / Durum Güncelle (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        if (reviewDetails.getUserName() != null) review.setUserName(reviewDetails.getUserName());
        if (reviewDetails.getRating() > 0) review.setRating(reviewDetails.getRating());
        if (reviewDetails.getComment() != null) review.setComment(reviewDetails.getComment());
        if (reviewDetails.getStatus() != null) review.setStatus(reviewDetails.getStatus());
        if (reviewDetails.getAdminReply() != null) review.setAdminReply(reviewDetails.getAdminReply());

        Review updatedReview = reviewRepository.save(review);
        return ResponseEntity.ok(updatedReview);
    }

    // Yorum Sil (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reviewRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}