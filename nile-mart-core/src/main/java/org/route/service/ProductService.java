package org.route.service;

import org.route.Category;
import org.route.Product;
import org.route.common.*;
import org.route.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository;
    private final PricingService pricingService;


    public ProductService(ProductRepository productRepository, PricingService pricingService) {
        this.productRepository = productRepository;
        this.pricingService = pricingService;
    }

    public List<Product> findAll(){
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getCategory)
                        .thenComparing(Product::getName))
                .toList();
    }

    public Product findBySku(String sku){
        return Check.found(productRepository.findBySku(sku) ,
                ()-> new NotFoundException("Product " + sku + " was not found."));
    }

    public BigDecimal getPrice(String sku, int quantity){
        if (quantity <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }

        Product product = findBySku(sku);

        BigDecimal total = pricingService.calculatePrice(product,quantity);
        return Money.scale(total);
    }

    public void reduceStock(String sku, int quantity) {
        if (quantity <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }

        Product product = findBySku(sku);

        if (product.getStockQuantity() < quantity) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity()
                            + " is available for " + product.getName() + "."
            );
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
    }

    public List<Product> search(Predicate<Product> filter){
        return productRepository.findAll().stream()
                .filter(filter)
                .toList();
    }

    public Map<Category, List<Product>> groupByCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.toList()
                ));
    }

}