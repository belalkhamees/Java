
package org.route.controller;

import org.route.cart.CartRegistry;
import org.route.cart.CartView;
import org.route.checkout.CheckoutService;
import org.route.checkout.Quote;
import org.route.model.Governorate;
import org.route.model.Order;
import org.route.cart.ShoppingCart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CheckoutController {

    private final CartRegistry cartRegistry;
    private final CheckoutService checkoutService;

    public CheckoutController(
            CartRegistry cartRegistry,
            CheckoutService checkoutService) {
        this.cartRegistry = cartRegistry;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/carts/{number}/checkout")
    public String checkout(
            @PathVariable("number") int number,
            @RequestParam(name = "governorate", required = false) Governorate governorate,
            Model model) {

        ShoppingCart shoppingCart = cartRegistry.get(number);
        CartView cart = shoppingCart.view();

        Quote quote = null;

        if (governorate != null) {
            quote = checkoutService.quote(shoppingCart, governorate);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("governorates", Governorate.values());
        model.addAttribute("selected", governorate);
        model.addAttribute("quote", quote);

        return "checkout/form";
    }

    @PostMapping("/carts/{number}/checkout")
    public String placeOrder(
            @PathVariable("number") int number,
            @RequestParam("governorate") Governorate governorate) {

        ShoppingCart cart = cartRegistry.get(number);

        Order order = checkoutService.placeOrder(cart, governorate);

        return "redirect:/orders/" + order.orderNumber();
    }
}
