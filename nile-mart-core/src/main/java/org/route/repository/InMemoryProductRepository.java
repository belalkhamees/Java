package org.route.repository;

import org.route.Category;
import org.route.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new ConcurrentHashMap<>();


    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Product save(Product product) {
        products.put(product.getSku(), product);
        return product;
    }


    public void loadProducts() {

        save(Product.builder()
                .sku("ACC-CHG-20W")
                .name("Fast Charger 20W")
                .category(Category.ELECTRONICS)
                .price(new BigDecimal("650.00"))
                .stockQuantity(20)
                .build());

        save(Product.builder()
                .sku("ACC-USB-C")
                .name("USB-C Cable")
                .category(Category.ELECTRONICS)
                .price(new BigDecimal("150.00"))
                .stockQuantity(30)
                .build());

        save(Product.builder()
                .sku("FAS-TSH-01")
                .name("Basic Cotton T-Shirt")
                .category(Category.FASHION)
                .price(new BigDecimal("350.00"))
                .stockQuantity(15)
                .build());

        save(Product.builder()
                .sku("FAS-JNS-01")
                .name("Slim Fit Jeans")
                .category(Category.FASHION)
                .price(new BigDecimal("900.00"))
                .stockQuantity(10)
                .build());

        save(Product.builder()
                .sku("HOM-PAN-28")
                .name("Non-stick Frying Pan 28cm")
                .category(Category.HOME_KITCHEN)
                .price(new BigDecimal("750.00"))
                .stockQuantity(3)
                .build());

        save(Product.builder()
                .sku("HOM-MUG-01")
                .name("Ceramic Coffee Mug")
                .category(Category.HOME_KITCHEN)
                .price(new BigDecimal("120.00"))
                .stockQuantity(25)
                .build());

        save(Product.builder()
                .sku("BEA-SUN-50")
                .name("Sun Cream SPF50")
                .category(Category.BEAUTY)
                .price(new BigDecimal("450.00"))
                .stockQuantity(0)
                .build());

        save(Product.builder()
                .sku("BOO-JAVA-01")
                .name("Java Programming Book")
                .category(Category.BOOKS)
                .price(new BigDecimal("550.00"))
                .stockQuantity(12)
                .build());
    }

}

