
package org.route.controller;

import org.route.cart.CartRegistry;
import org.route.cart.CartView;
import org.route.cart.ShoppingCart;
import org.route.controller.form.AddItemForm;
import org.route.controller.form.NewCartForm;
import org.route.model.Customer;
import org.route.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private final CartRegistry cartRegistry;
    private final ProductService productService;

    public CartController(CartRegistry cartRegistry, ProductService productService) {
        this.cartRegistry = cartRegistry;
        this.productService = productService;
    }

    @GetMapping("/carts")
    public String carts(Model model) {
        model.addAttribute("carts", cartRegistry.views());
        return "carts/list";
    }

    @GetMapping("/carts/new")
    public String newCart(Model model) {
        model.addAttribute("form", new NewCartForm());
        return "carts/new";
    }

    @PostMapping("/carts")
    public String createCart(
            @ModelAttribute("form") NewCartForm form,
            RedirectAttributes redirectAttributes) {

        String email = form.getEmail();
        if (email != null && email.isBlank()) {
            email = null;
        }

        Customer customer = new Customer(
                form.getFullName(),
                form.getPhone(),
                email
        );

        ShoppingCart cart = cartRegistry.open(customer);

        redirectAttributes.addFlashAttribute(
                "message", "Cart created successfully."
        );

        return "redirect:/carts/" + cart.getNumber();
    }

    @GetMapping("/carts/{number}")
    public String cart(@PathVariable("number") int number, Model model) {
        CartView cart = cartRegistry.get(number).view();

        model.addAttribute("cart", cart);
        model.addAttribute(
                "products",
                productService.search(product -> product.getStockQuantity() > 0)
        );

        return "carts/view";
    }

    @PostMapping("/carts/items")
    public String addItem(
            @ModelAttribute AddItemForm form,
            RedirectAttributes redirectAttributes) {

        cartRegistry.get(form.getCartNumber())
                .addItem(form.getSku(), form.getQuantity());

        redirectAttributes.addFlashAttribute(
                "message", "Product added to cart successfully."
        );

        return "redirect:/carts/" + form.getCartNumber();
    }
}
