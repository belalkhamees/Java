package org.route.cart;

import java.math.BigDecimal;

public record LineView(
        String sku,
        String name,
        int quantity,
        BigDecimal linePrice
) {
}