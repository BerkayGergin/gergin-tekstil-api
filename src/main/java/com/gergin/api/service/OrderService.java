package com.gergin.api.service;

import com.gergin.api.entity.Order;
import com.gergin.api.entity.OrderItem;
import com.gergin.api.entity.Product;
import com.gergin.api.repository.OrderRepository;
import com.gergin.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public Order processOrder(Order order) {
        // 1. Siparişteki her bir ürünün stoğunu kontrol et ve düşür
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Ürün bulunamadı! ID: " + item.getProductId()));

                // Stok yeterlilik kontrolü
                if (product.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Yetersiz stok! '" + product.getName() + "' parçasından sadece " + product.getStock() + " adet kaldı.");
                }

                // Stoğu düş ve güncelle
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product); // @Version sayesinde eşzamanlı çakışmada otomatik koruma sağlar
            }
        }

        // 2. Siparişi veritabanına kaydet
        order.setStatus("ONAYLANDI");
        return orderRepository.save(order);
    }
}