package org.route.cart;

import org.route.model.Customer;

import java.math.BigDecimal;
import java.util.List;

public record CartView(
        int number,
        Customer customer,
        List<LineView> lines,
        int itemCount,
        BigDecimal subtotal,
        boolean empty
) {
}