package org.route.repository;

import jakarta.annotation.PreDestroy;
import org.route.Category;
import org.route.Product;
import org.route.common.StoreException;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;


@Repository
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


    @PostConstruct
    public void loadProducts() {

        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("products.csv");

        if (inputStream == null) {
            throw new StoreException("The catalog could not be loaded.",
                    new IOException("products.csv was not found."));
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream))) {

            // Skip the header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",");

                Product product = Product.builder()
                        .sku(fields[0])
                        .name(fields[1])
                        .category(Category.valueOf(fields[2]))
                        .price(new BigDecimal(fields[3]))
                        .stockQuantity(Integer.parseInt(fields[4]))
                        .build();

                save(product);
            }

            System.out.println("Catalog loaded: " + products.size() + " products.");

        } catch (IOException e) {
            throw new StoreException("The catalog could not be loaded.", e);
        }
    }

    @PreDestroy
    public void closeCatalog() {
        System.out.println("Catalog closed.");
    }
}

