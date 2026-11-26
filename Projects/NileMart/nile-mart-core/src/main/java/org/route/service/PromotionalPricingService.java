
package org.route.service;

import org.route.model.Product;
import org.route.common.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PromotionalPricingService implements PricingService {

    @Value("${store.discount-amount}")
    private BigDecimal discountAmount;

    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        BigDecimal discountRate = discountAmount.movePointLeft(2);
        BigDecimal discount = total.multiply(discountRate);

        return Money.scale(total.subtract(discount));
    }
}
