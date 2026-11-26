package org.route.service;

import org.route.model.Product;
import org.route.common.Money;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class StandardPricingService implements PricingService {

    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        return Money.scale(total);
    }

}
