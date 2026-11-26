
package org.route.checkout;

import lombok.RequiredArgsConstructor;
import org.route.cart.ShoppingCart;
import org.route.common.Money;
import org.route.common.StoreException;
import org.route.model.*;
import org.route.service.ProductService;
import org.route.shipping.ShippingQuoteService;
import org.route.notification.NotificationSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> senders;

    public Quote quote(ShoppingCart cart, Governorate g) {

        if (cart.isEmpty()) {
            throw new StoreException("Cannot checkout an empty cart.");
        }

        BigDecimal subtotal = cart.subtotal();
        BigDecimal vat = taxCalculator.calculate(subtotal);
        BigDecimal shippingFee = shippingQuoteService.quote(g, subtotal);
        BigDecimal total = Money.scale(subtotal.add(vat).add(shippingFee));

        return new Quote(subtotal, vat, shippingFee, total);
    }

    public synchronized Order placeOrder(ShoppingCart cart, Governorate g) {

        // 1. Reject empty cart
        if (cart.isEmpty()) {
            throw new StoreException("Cannot checkout an empty cart.");
        }

        // 2. Check stock of all lines without changing anything
        for (Map.Entry<String, Integer> entry : cart.lines().entrySet()) {
            productService.checkStock(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        // 3. Calculate quote
        Quote quote = quote(cart, g);

        // 4. Reduce stock of all lines
        for (Map.Entry<String, Integer> entry : cart.lines().entrySet()) {
            productService.reduceStock(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        // 5. Generate order number
        String orderNumber = orderRegistry.nextNumber();

        // 6. Create order lines
        List<OrderLine> orderLines = cart.lines().entrySet().stream()
                .map(entry -> {
                    Product product = productService.findBySku(entry.getKey());

                    return new OrderLine(
                            product.getSku(),
                            product.getName(),
                            entry.getValue(),
                            productService.getPrice(
                                    entry.getKey(),
                                    entry.getValue()
                            )
                    );
                })
                .toList();

        // 7. Create and save order
        Order order = new Order(
                orderNumber,
                cart.getCustomer(),
                g,
                orderLines,
                quote.subtotal(),
                quote.vat(),
                quote.shippingFee(),
                quote.total()
        );

        orderRegistry.save(order);

        // 8. Notify through every sender
        String message = "Your order " + order.orderNumber()
                + " has been placed successfully. Total: "
                + Money.format(order.total()) + " EGP.";

        senders.forEach(sender ->
                sender.send(order.customer(), message)
        );

        // 9. Clear cart and return order
        cart.clear();

        return order;
    }
}
