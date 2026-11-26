package org.route.model;

import org.route.common.Money;

import java.math.BigDecimal;
import java.util.List;

public record Order(
        String orderNumber,
        Customer customer,
        Governorate governorate,
        List<OrderLine> lines,
        BigDecimal subtotal,
        BigDecimal vat,
        BigDecimal shippingFee,
        BigDecimal total
) {
    public Order {
        lines = List.copyOf(lines);

        subtotal = Money.scale(subtotal);
        vat = Money.scale(vat);
        shippingFee = Money.scale(shippingFee);
        total = Money.scale(total);
    }
}