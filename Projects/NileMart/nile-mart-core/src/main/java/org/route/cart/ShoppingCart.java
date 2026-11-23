
package org.route.cart;

import lombok.Getter;
import org.route.Product;
import org.route.common.Money;
import org.route.common.OutOfStockException;
import org.route.common.StoreException;
import org.route.model.Customer;
import org.route.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantityPerProduct;

    @Getter
    private int number;

    @Getter
    private Customer customer;

    private boolean opened;

    private final Map<String, Integer> lines = new LinkedHashMap<>();

    public ShoppingCart(
            ProductService productService,
            @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {

        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }

    public void open(int number, Customer customer) {

        if (opened) {
            throw new IllegalStateException("Cart is already open.");
        }

        this.number = number;
        this.customer = customer;
        this.opened = true;
    }


    public synchronized void addItem(String sku, int qty) {

        // 1. Validate requested quantity
        if (qty < 1) {
            throw new StoreException("Quantity must be at least 1.");
        }

        // 2. Find the product
        Product product = productService.findBySku(sku);

        // 3. Check if product is out of stock
        if (product.getStockQuantity() == 0) {
            throw new OutOfStockException(
                    product.getName() + " is out of stock."
            );
        }

        // 4. Calculate the new quantity
        int newQty = lines.getOrDefault(sku, 0) + qty;

        // 5. Check maximum quantity
        if (newQty > maxQuantityPerProduct) {
            throw new StoreException(
                    "You can add at most " + maxQuantityPerProduct
                            + " of " + product.getName() + "."
            );
        }

        // 6. Check available stock
        if (newQty > product.getStockQuantity()) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity()
                            + " available for " + product.getName() + "."
            );
        }

        // 7. Update cart only after all checks pass
        lines.put(sku, newQty);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void clear() {
        lines.clear();
    }

    public Map<String, Integer> lines() {
        return Collections.unmodifiableMap(lines);
    }

    public BigDecimal subtotal() {
        return lines.entrySet().stream()
                .map(entry -> productService.getPrice(
                        entry.getKey(),
                        entry.getValue()
                ))
                .reduce(Money.ZERO, BigDecimal::add);
    }


    public CartView view() {

        List<LineView> lineViews = lines.entrySet().stream()
                .map(entry -> {
                    Product product = productService.findBySku(entry.getKey());

                    return new LineView(
                            product.getSku(),
                            product.getName(),
                            entry.getValue(),
                            productService.getPrice(entry.getKey(), entry.getValue())
                    );
                })
                .toList();

        int itemCount = lines.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        return new CartView(
                number,
                customer,
                List.copyOf(lineViews),
                itemCount,
                subtotal(),
                isEmpty()
        );
    }

}
