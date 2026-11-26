package org.route.checkout;

import org.route.common.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxCalculator {

    @Value("${store.vat-rate}")
    private BigDecimal vatRate;

    public BigDecimal calculate(BigDecimal subtotal) {
        return Money.scale(subtotal.multiply(vatRate));
    }
}