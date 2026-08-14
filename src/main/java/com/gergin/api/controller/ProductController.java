package com.gergin.api.controller;

import com.gergin.api.entity.Order;
import com.gergin.api.entity.OrderItem;
import com.gergin.api.entity.Product;
import com.gergin.api.repository.OrderRepository;
import com.gergin.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 1. Vitrin İçin Tüm Ürünleri Getir
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. Ürün İnceleme Sayfası İçin Tek Bir Ürün Getir
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Koleksiyona Yeni Ürün Ekle
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 4. Koleksiyondan Ürün Sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 5. Güvenli Satın Alma ve Sipariş Oluşturma İşlemi
    @PostMapping("/purchase")
    public ResponseEntity<String> checkout(@RequestBody Map<String, Object> request) {
        try {
            Object userIdObj = request.get("userId");
            Object productIdsObj = request.get("productIds");

            if (userIdObj == null || productIdsObj == null) {
                return ResponseEntity.badRequest().body("Eksik veri: userId veya productIds bulunamadı.");
            }

            Long userId = Long.valueOf(userIdObj.toString());
            List<?> rawList = (List<?>) productIdsObj;
            List<Long> productIds = new ArrayList<>();
            for (Object item : rawList) {
                productIds.add(Long.valueOf(item.toString()));
            }

            // Yeni Sipariş Nesnesi Oluştur
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalPrice(0.0);

            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0.0;

            for (Long id : productIds) {
                Product product = productRepository.findById(id).orElse(null);

                if (product != null) {
                    // Stok Düşürme (Zırhlı Mantık)
                    if (product.getStock() != null && product.getStock() > 0) {
                        product.setStock(product.getStock() - 1);
                    } else if (product.getStockQuantity() != null && product.getStockQuantity() > 0) {
                        product.setStockQuantity(product.getStockQuantity() - 1);
                    }
                    productRepository.save(product);

                    // Fiyatı Güvenli Alış (BigDecimal -> Double Dönüşümü)
                    double priceVal = 0.0;
                    if (product.getPrice() != null) {
                        priceVal = product.getPrice().doubleValue();
                    }

                    // Sipariş Kalemi (Item) Ekle
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductName(product.getName());
                    orderItem.setPrice(priceVal);
                    orderItem.setOrder(order);
                    orderItems.add(orderItem);

                    totalPrice += priceVal;
                }
            }

            order.setTotalPrice(totalPrice);
            order.setItems(orderItems);

            // Siparişi ve kalemlerini veritabanına kalıcı olarak kaydet
            orderRepository.save(order);

            return ResponseEntity.ok("Sipariş başarıyla oluşturuldu ve stoklar güncellendi.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Sipariş işlenirken hata oluştu: " + e.getMessage());
        }
    }
}