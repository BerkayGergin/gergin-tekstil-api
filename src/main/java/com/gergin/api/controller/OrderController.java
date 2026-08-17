package com.gergin.api.controller;

import com.gergin.api.entity.Order;
import com.gergin.api.entity.OrderItem;
import com.gergin.api.entity.Product;
import com.gergin.api.entity.User;
import com.gergin.api.repository.OrderRepository;
import com.gergin.api.repository.ProductRepository;
import com.gergin.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 1. Admin Paneli İçin Tüm Siparişleri Listele
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 2. Kullanıcının Siparişlerini Getirir
    @GetMapping("/{identifier}")
    public List<Order> getUserOrders(@PathVariable String identifier) {
        try {
            Long id = Long.parseLong(identifier);
            return orderRepository.findByUserId(id);
        } catch (NumberFormatException e) {
            User user = userRepository.findByUsername(identifier).orElse(null);
            if (user != null) {
                return orderRepository.findByUserId(user.getId());
            }
            return Collections.emptyList();
        }
    }

    // 3. Sipariş Durumu & Kargo Takip Bilgilerini Güncelle (Admin İçin)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(required = false) String cargoCompany) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        order.setStatus(status);
        if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
            order.setTrackingNumber(trackingNumber.trim());
        }
        if (cargoCompany != null && !cargoCompany.trim().isEmpty()) {
            order.setCargoCompany(cargoCompany.trim());
        }
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            // 1. Önce siparişi kaydet (ID oluşsun)
            if (order.getStatus() == null || order.getStatus().isEmpty()) {
                order.setStatus("Hazırlanıyor");
            }
            // items listesini geçici olarak sakla
            List<OrderItem> items = order.getItems();
            order.setItems(null);
            Order savedOrder = orderRepository.save(order);

            // 2. Stokları düş ve ürünleri kaydet
            if (items != null) {
                for (OrderItem item : items) {
                    Product product = productRepository.findByIdForUpdate(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı!"));

                    int requestedQuantity = item.getQuantity() != null ? item.getQuantity() : 1;

                    if (product.getStock() < requestedQuantity) {
                        throw new RuntimeException("Yetersiz stok!");
                    }

                    // Stoğu düş
                    product.setStock(product.getStock() - requestedQuantity);
                    product.setStockQuantity(product.getStock());
                    productRepository.saveAndFlush(product);

                    // Ürünleri siparişe bağla
                    item.setOrder(savedOrder);
                    // Burada ürünleri ayrı bir repository ile kaydedebilirsin
                    // Eğer OrderItemRepository'n yoksa, doğrudan EntityManager ile persist edebilirsin
                    // Ya da OrderItemRepository oluşturmalısın:
                    // orderItemRepository.save(item);
                }
            }

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }
}