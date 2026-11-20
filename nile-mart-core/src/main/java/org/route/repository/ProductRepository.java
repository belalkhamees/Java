package org.route.repository;

import org.route.Product;

import java.util.Optional;

public interface ProductRepository extends Repository<Product, String> {

    default Optional<Product> findBySku(String sku) {
        return findById(sku);
    }
}