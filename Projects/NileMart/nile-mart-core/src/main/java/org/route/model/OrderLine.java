package org.route.model;

import java.math.BigDecimal;

public record OrderLine(
        String sku,
        String name,
        int quantity,
        BigDecimal linePrice
) {
}