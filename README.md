# ⚙️ Gergin Tekstil — RESTful E-Commerce Backend API

Gergin Tekstil e-ticaret platformunun ürün, stok, sipariş ve değerlendirme operasyonlarını yöneten Spring Boot tabanlı REST API servisi.

---

## ✨ Öne Çıkan Özellikler

- **Koleksiyon & Ürün Yönetimi:** Beden bazlı stok haritaları (`sizeStock`) ve genel stok takibi.
- **Stok Bütünlüğü & Sipariş Akışı:** Sipariş anında veritabanı düzeyinde atomik stok düşümü ve doğrulama.
- **Değerlendirme Sistemi:** Ürünlere özel müşteri yorumları, yıldız puanlaması ve yönetici yanıt desteği.
- **RESTful Standartları:** Temiz uç noktalar, katmanlı servis mimarisi ve DTO pattern kullanımı.

---

## 🛠️ Teknoloji Yığını

- **Dil:** Java (17+)
- **Framework:** Spring Boot 3
- **Veritabanı:** PostgreSQL / Neon Serverless DB
- **ORM / Veri Erişimi:** Spring Data JPA (Hibernate)
- **Doğrulama:** Jakarta Validation
- **Derleme Aracı:** Maven

---

## 🔌 API Uç Noktaları

| Metot | Uç Nokta | Açıklama |
| :--- | :--- | :--- |
| `GET` | `/api/products` | Koleksiyondaki tüm parçaları listeler |
| `GET` | `/api/products/{id}` | Belirtilen parçanın anlık detayını getirir |
| `POST` | `/api/products` | Yeni koleksiyon parçası ekler |
| `DELETE` | `/api/products/{id}` | Parçayı veritabanından siler |
| `POST` | `/api/orders` | Sipariş oluşturur ve stokları günceller |
| `GET` | `/api/reviews/product/{productId}` | Ürüne ait onaylanmış değerlendirmeleri getirir |
| `POST` | `/api/reviews` | Yeni müşteri değerlendirmesi kaydeder |

---

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler
- JDK 17 veya üzeri
- Maven
- PostgreSQL veya Neon DB bağlantısı

### Adımlar

1. Depoyu klonlayın:
   ```bash
   git clone [https://github.com/kullanici-adi/gergin-tekstil-backend.git](https://github.com/kullanici-adi/gergin-tekstil-backend.git)
   cd gergin-tekstil-backend
