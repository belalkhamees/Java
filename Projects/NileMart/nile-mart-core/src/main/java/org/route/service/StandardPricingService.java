package org.route.service;

import org.route.Product;
import org.route.common.Money;

import java.math.BigDecimal;

public class StandardPricingService implements PricingService {

    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
         BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
         return Money.scale(total);
    }

}
