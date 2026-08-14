package com.gergin.api.controller;

import com.gergin.api.entity.Order;
import com.gergin.api.entity.User;
import com.gergin.api.repository.OrderRepository;
import com.gergin.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Kullanıcının siparişlerini ID veya Username ile getirir
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
}