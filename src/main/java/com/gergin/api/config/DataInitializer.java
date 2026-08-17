package com.gergin.api;

import com.gergin.api.entity.Product;
import com.gergin.api.entity.User;
import com.gergin.api.repository.ProductRepository;
import com.gergin.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. Admin Kullanıcı Kontrolü
            if (userRepository.findByUsername("superadmin").isEmpty()) {
                User admin = new User();
                admin.setUsername("superadmin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("Superadmin oluşturuldu.");
            }

            // 2. Yalnızca Veritabanı Tamamen Boşken Başlangıç Ürünlerini Ekle
            if (productRepository.count() == 0) {
                productRepository.save(new Product(
                        "Kruvaze Kaşmir Kaban", 9850.0, "Dış Giyim", 14, 14,
                        "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?auto=format&fit=crop&w=800&q=80",
                        "%100 İtalyan kaşmir yün kumaş, geniş kruvaze yaka, kuşağı ve dökümlü relaxed kesimiyle zamansız bir silüet.",
                        "{\"36 (S)\": 4, \"38 (M)\": 6, \"40 (L)\": 4, \"42 (XL)\": 0}"
                ));

                productRepository.save(new Product(
                        "Alpaka Yün Oversize Hırka", 3950.0, "Triko & Hırka", 19, 19,
                        "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?auto=format&fit=crop&w=800&q=80",
                        "Doğal alpaka ve merinos liflerinden örülmüş, boynuz düğmeli, yumuşak dokulu ve dökümlü lüks triko hırka.",
                        "{\"XS/S\": 8, \"M/L\": 11}"
                ));

                productRepository.save(new Product(
                        "Yün Yelek & Pileli Pantolon Takım", 7450.0, "Takım", 9, 9,
                        "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?auto=format&fit=crop&w=800&q=80",
                        "Kusursuz terzilik işçiliği ile dikilmiş kruvaze yelek ve yüksek bel çift pileli palazzo pantolondan oluşan iki parçalı takım.",
                        "{\"36\": 3, \"38\": 4, \"40\": 2}"
                ));

                productRepository.save(new Product(
                        "Dökümlü Saf İpek Bluz", 3150.0, "Gömlek & Bluz", 15, 15,
                        "https://images.unsplash.com/photo-1589310243389-96a5483213a8?auto=format&fit=crop&w=800&q=80",
                        "22 momme saf dut ipeği, gizli pat düğme detayı ve akıcı dökümü ile hem gündüz hem gece için rafine bir şıklık.",
                        "{\"36 (S)\": 5, \"38 (M)\": 6, \"40 (L)\": 4}"
                ));

                productRepository.save(new Product(
                        "Çift Yüzlü Yün Blazer Ceket", 8250.0, "Ceket", 8, 8,
                        "https://images.unsplash.com/photo-1548624149-f9b1859aa7d0?auto=format&fit=crop&w=800&q=80",
                        "Double-face saf yün kumaş, astarsız el dikişi kenar bitişleri ve modern minimal kalıp.",
                        "{\"36\": 2, \"38\": 4, \"40\": 2}"
                ));

                productRepository.save(new Product(
                        "Yüksek Bel Akıcı Palazzo Pantolon", 3450.0, "Pantolon", 12, 12,
                        "https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=800&q=80",
                        "Yün ve viskon karışımlı, dökümlü geniş paça kesim ve zarif bel kemeri detayı.",
                        "{\"34 (XS)\": 3, \"36 (S)\": 4, \"38 (M)\": 3, \"40 (L)\": 2}"
                ));

                productRepository.save(new Product(
                        "Fitilli Merinos Yün Triko Elbise", 4850.0, "Elbise", 10, 10,
                        "https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=800&q=80",
                        "Vücudu saran esnek fitilli merinos örgü, dik yaka ve midi boy zarif yırtmaç detayı.",
                        "{\"S\": 4, \"M\": 4, \"L\": 2}"
                ));

                productRepository.save(new Product(
                        "Oversize Hakiki Deri Trençkot", 11200.0, "Dış Giyim", 0, 0,
                        "https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=800&q=80",
                        "Yumuşak kuzu derisi, kruvaze kapama, geniş yaka ve manşet kemer detaylı özel seri dış giyim parçası.",
                        "{\"36\": 0, \"38\": 0, \"40\": 0}"
                ));
            }
        };
    }
}