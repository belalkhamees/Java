
package org.route.service;

import org.route.Product;
import org.route.common.Money;

import java.math.BigDecimal;

public class PromotionalPricingService implements PricingService{

    private static final BigDecimal DISCOUNT = new BigDecimal("0.20");


    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        BigDecimal discount = total.multiply(DISCOUNT);
        return Money.scale(total.subtract(discount));
    }
}



