
package org.route.controller;

import org.route.cart.CartRegistry;
import org.route.controller.form.*;
import org.route.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartRegistry cartRegistry;

    public ProductController(
            ProductService productService,
            CartRegistry cartRegistry) {

        this.productService = productService;
        this.cartRegistry = cartRegistry;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) Integer cart,
            Model model) {

        model.addAttribute("groups", productService.groupByCategory());
        model.addAttribute("carts", cartRegistry.views());
        model.addAttribute("selectedCart", cart);

        return "products/list";
    }
}
