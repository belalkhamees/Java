
package org.route.controller;

import org.route.checkout.OrderRegistry;
import org.route.common.Check;
import org.route.common.NotFoundException;
import org.route.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

    private final OrderRegistry orderRegistry;

    public OrderController(OrderRegistry orderRegistry) {
        this.orderRegistry = orderRegistry;
    }

    @GetMapping("/orders/{orderNumber}")
    public String confirmation(
            @PathVariable("orderNumber") String orderNumber,
            Model model) {

        Order order = Check.found(
                orderRegistry.findByNumber(orderNumber),
                () -> new NotFoundException(
                        "Order " + orderNumber + " was not found."
                )
        );

        model.addAttribute("order", order);

        return "orders/confirmation";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRegistry.findAll());
        model.addAttribute("revenue", orderRegistry.revenue());

        return "orders/list";
    }
}
